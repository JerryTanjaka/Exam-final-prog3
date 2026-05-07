package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.member.MemberDescription;
import hei.fprog3.dto.statistic.CollectivityOverallStatistics;
import hei.fprog3.dto.statistic.MemberStatistic;
import hei.fprog3.model.enums.PositionType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StatisticRepository {
    private final DataSourceConfig dataSource;

    public StatisticRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    public List<MemberStatistic> getCollectivityMemberStatistic(String collectivityId, LocalDate from, LocalDate to) {
        Connection connection = dataSource.getConnection();
        try {
            // 1. Récupérer les membres actifs
            PreparedStatement memberPs = connection.prepareStatement("""
                SELECT m.id, m.first_name, m.last_name, m.email, ms.occupation
                FROM members m
                JOIN memberships ms ON m.id = ms.member_id
                WHERE ms.collectivity_id = ? AND ms.end_date IS NULL
                ORDER BY m.id
                """);
            memberPs.setString(1, collectivityId);
            ResultSet memberRs = memberPs.executeQuery();

            List<MemberStatistic> result = new ArrayList<>();

            // 2. Préparer les requêtes de détails (Finances et Assiduité)
            // On sépare pour éviter les jointures cartésiennes qui faussent les SUM
            PreparedStatement financePs = connection.prepareStatement("""
                SELECT 
                    (SELECT COALESCE(SUM(amount), 0) FROM payments 
                     WHERE member_id = ? AND creation_date BETWEEN ? AND ?) as earned,
                    (SELECT COALESCE(SUM(amount), 0) FROM fees 
                     WHERE collectivity_id = ? AND status = 'ACTIVE' AND eligible_from BETWEEN ? AND ?) as total_due
                """);

            PreparedStatement assiduityPs = connection.prepareStatement("""
                SELECT 
                    COUNT(DISTINCT a.id) as total_req,
                    COUNT(DISTINCT aa.id) FILTER (WHERE aa.status = 'ATTENDED') as attended
                FROM activities a
                JOIN activity_required_members arm ON a.id = arm.activity_id
                LEFT JOIN activity_attendances aa ON a.id = aa.activity_id AND aa.member_id = ?
                WHERE a.collectivity_id = ? 
                AND (
                    a.executive_date BETWEEN ? AND ? 
                    OR (a.executive_date IS NULL AND aa.id IS NOT NULL) -- Pour les activités récurrentes
                )
                AND arm.required_member = ?::position_type
                """);

            while (memberRs.next()) {
                String mId = memberRs.getString("id");
                String occ = memberRs.getString("occupation");

                // Calcul Finance
                financePs.setString(1, mId);
                financePs.setDate(2, Date.valueOf(from));
                financePs.setDate(3, Date.valueOf(to));
                financePs.setString(4, collectivityId);
                financePs.setDate(5, Date.valueOf(from));
                financePs.setDate(6, Date.valueOf(to));
                ResultSet fRs = financePs.executeQuery();
                fRs.next();
                double earned = fRs.getDouble("earned");
                double due = Math.max(0, fRs.getDouble("total_due") - earned);

                // Calcul Assiduité
                assiduityPs.setString(1, mId);
                assiduityPs.setString(2, collectivityId);
                assiduityPs.setDate(3, Date.valueOf(from));
                assiduityPs.setDate(4, Date.valueOf(to));
                assiduityPs.setString(5, occ);
                ResultSet aRs = assiduityPs.executeQuery();
                aRs.next();

                int totalReq = aRs.getInt("total_req");
                double assiduity = (totalReq == 0) ? 100.0 : (aRs.getInt("attended") * 100.0 / totalReq);

                result.add(new MemberStatistic(
                        new MemberDescription(mId, memberRs.getString("first_name"), memberRs.getString("last_name"), memberRs.getString("email"), PositionType.valueOf(occ)),
                        earned, due, assiduity
                ));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        Connection connection = dataSource.getConnection();
        try {
            // Récupération des collectivités et nouveaux membres
            PreparedStatement colPs = connection.prepareStatement("""
                SELECT c.id, c.name, c.number,
                       (SELECT COUNT(*) FROM memberships ms WHERE ms.collectivity_id = c.id AND ms.end_date IS NULL) as total_m,
                       (SELECT COUNT(*) FROM memberships ms WHERE ms.collectivity_id = c.id AND ms.start_date BETWEEN ? AND ?) as new_m
                FROM collectivities c ORDER BY c.name
                """);
            colPs.setDate(1, Date.valueOf(from));
            colPs.setDate(2, Date.valueOf(to));
            ResultSet colRs = colPs.executeQuery();

            List<CollectivityOverallStatistics> stats = new ArrayList<>();

            while (colRs.next()) {
                String id = colRs.getString("id");
                int totalM = colRs.getInt("total_m");

                // Calcul du % à jour pour cette collectivité
                // Un membre est à jour s'il n'a aucun impayé sur les cotisations actives de la période
                PreparedStatement upToDatePs = connection.prepareStatement("""
                    SELECT COUNT(*) FROM memberships ms
                    WHERE ms.collectivity_id = ? AND ms.end_date IS NULL
                    AND NOT EXISTS (
                        SELECT 1 FROM fees f
                        WHERE f.collectivity_id = ms.collectivity_id 
                        AND f.status = 'ACTIVE' AND f.eligible_from BETWEEN ? AND ?
                        AND (SELECT COALESCE(SUM(amount), 0) FROM payments p 
                             WHERE p.member_id = ms.member_id AND p.membership_fee_id = f.id) < f.amount
                    )
                    """);
                upToDatePs.setString(1, id);
                upToDatePs.setDate(2, Date.valueOf(from));
                upToDatePs.setDate(3, Date.valueOf(to));
                ResultSet utdRs = upToDatePs.executeQuery();
                utdRs.next();
                double utdPct = (totalM == 0) ? 0.0 : (utdRs.getInt(1) * 100.0 / totalM);

                // Calcul de l'assiduité globale (moyenne des assiduités individuelles)
                List<MemberStatistic> memberStats = getCollectivityMemberStatistic(id, from, to);
                double avgAssiduity = memberStats.isEmpty() ? 100.0 :
                        memberStats.stream().mapToDouble(MemberStatistic::getAssiduityPercentage).average().orElse(100.0);

                stats.add(new CollectivityOverallStatistics(
                        new CollectivityInformation(colRs.getString("name"), colRs.getInt("number")),
                        colRs.getInt("new_m"),
                        utdPct,
                        avgAssiduity
                ));
            }
            return stats;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}
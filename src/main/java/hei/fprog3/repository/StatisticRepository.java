package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.member.MemberDescription;
import hei.fprog3.dto.statistic.CollectivityOverallStatistics;
import hei.fprog3.dto.statistic.MemberStatistic;
import hei.fprog3.exception.NotFoundException;
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
    private DataSourceConfig dataSource;
    public StatisticRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }
    public List<MemberStatistic> getCollectivityMemberStatistic(String collectivityId, LocalDate from, LocalDate to) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement memberPs = connection.prepareStatement(
                    """
                    SELECT m.id, m.first_name, m.last_name, m.email, ms.occupation FROM members AS m
                    JOIN memberships AS ms ON m.id = ms.member_id
                    WHERE ms.collectivity_id = ?
                    GROUP BY m.id, ms.occupation
                    ORDER BY m.id, MAX(ms.start_date) DESC
                    """);
            memberPs.setString(1, collectivityId);

            PreparedStatement amountPs = connection.prepareStatement(
                    """
                    WITH total_earned AS (
                        SELECT SUM(p.amount) AS earnedAmount
                        FROM payments AS p
                            JOIN fees AS f ON f.id = p.membership_fee_id
                        WHERE f.collectivity_id = ? AND p.member_id = ?
                            AND p.creation_date BETWEEN ? AND ?
                    ), total_active_fees AS (
                        SELECT SUM(CASE f.status WHEN 'ACTIVE' THEN f.amount ELSE 0 END) AS total_fees
                        FROM fees AS f
                        WHERE f.collectivity_id = ?
                            AND f.eligible_from BETWEEN ? AND ?
                    )
                    SELECT earnedAmount, (total_fees::FLOAT - earnedAmount::FLOAT)::FLOAT AS dueAmount
                    FROM total_earned, total_active_fees
                    """);
            amountPs.setString(1, collectivityId);
            amountPs.setDate(3, Date.valueOf(from));
            amountPs.setDate(4, Date.valueOf(to));
            amountPs.setString(5, collectivityId);
            amountPs.setDate(6, Date.valueOf(from));
            amountPs.setDate(7, Date.valueOf(to));

            PreparedStatement assiduityPs = connection.prepareStatement(
                    """
                    SELECT
                        COUNT(a.id)                                                         AS total_required,
                        COUNT(aa.id) FILTER (WHERE aa.status = 'ATTENDED')                 AS attended
                    FROM activities a
                    JOIN activity_required_members arm
                        ON arm.activity_id = a.id
                        AND arm.required_member = ?::position_type
                    LEFT JOIN activity_attendances aa
                        ON aa.activity_id = a.id
                        AND aa.member_id = ?
                    WHERE a.collectivity_id = ?
                      AND a.executive_date BETWEEN ? AND ?
                    """);
            assiduityPs.setString(3, collectivityId);
            assiduityPs.setDate(4, Date.valueOf(from));
            assiduityPs.setDate(5, Date.valueOf(to));

            ResultSet memberRs = memberPs.executeQuery();
            List<MemberStatistic> memberStatistics = new ArrayList<>();
            while (memberRs.next()) {
                amountPs.setString(2, memberRs.getString("id"));
                ResultSet amountRs = amountPs.executeQuery();
                if (!amountRs.next()) throw new RuntimeException("Empty amountRs");

                // NOUVEAU : calcul assiduité
                assiduityPs.setString(1, memberRs.getString("occupation")); // position_type
                assiduityPs.setString(2, memberRs.getString("id"));
                ResultSet assiduityRs = assiduityPs.executeQuery();
                double assiduityPercentage = 0.0;
                if (assiduityRs.next()) {
                    int total = assiduityRs.getInt("total_required");
                    int attended = assiduityRs.getInt("attended");
                    assiduityPercentage = (total == 0) ? 100.0 : (attended * 100.0 / total);
                }

                MemberDescription memberDescription = new MemberDescription();
                memberDescription.setId(memberRs.getString("id"));
                memberDescription.setFirstName(memberRs.getString("first_name"));
                memberDescription.setLastName(memberRs.getString("last_name"));
                memberDescription.setEmail(memberRs.getString("email"));
                memberDescription.setOccupation(PositionType.valueOf(memberRs.getString("occupation")));

                memberStatistics.add(new MemberStatistic(
                        memberDescription,
                        amountRs.getDouble("earnedAmount"),
                        amountRs.getDouble("dueAmount"),
                        assiduityPercentage
                ));
            }
            return memberStatistics;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        Connection connection = dataSource.getConnection();
        try {
            List<CollectivityOverallStatistics> result = new ArrayList<>();
            List<String> collectivityIds = new ArrayList<>();

            PreparedStatement collectivitiesPs = connection.prepareStatement("""
            SELECT c.id, c.name, c.number,
                   COUNT(ms.member_id)                                              AS total_members,
                   COUNT(ms.member_id) FILTER (WHERE ms.start_date BETWEEN ? AND ?) AS new_members
            FROM collectivities c
            LEFT JOIN memberships ms ON ms.collectivity_id = c.id AND ms.end_date IS NULL
            GROUP BY c.id, c.name, c.number
            ORDER BY c.name
        """);
            collectivitiesPs.setDate(1, Date.valueOf(from));
            collectivitiesPs.setDate(2, Date.valueOf(to));

            ResultSet collectivitiesRs = collectivitiesPs.executeQuery();
            List<Object[]> collectivitiesData = new ArrayList<>();
            while (collectivitiesRs.next()) {
                collectivitiesData.add(new Object[]{
                        collectivitiesRs.getString("id"),
                        collectivitiesRs.getString("name"),
                        collectivitiesRs.getInt("number"),
                        collectivitiesRs.getInt("total_members"),
                        collectivitiesRs.getInt("new_members")
                });
                collectivityIds.add(collectivitiesRs.getString("id"));
            }
            PreparedStatement upToDatePs = connection.prepareStatement("""
            SELECT
                ms.collectivity_id,
                ms.member_id,
                BOOL_AND(
                    paid_per_fee.paid_amount >= f.amount
                ) AS is_up_to_date
            FROM memberships ms
            JOIN fees f
                ON f.collectivity_id = ms.collectivity_id
                AND f.status = 'ACTIVE'
            LEFT JOIN (
                SELECT
                    p.member_id,
                    p.membership_fee_id,
                    SUM(p.amount) AS paid_amount
                FROM payments p
                WHERE p.creation_date BETWEEN ? AND ?
                GROUP BY p.member_id, p.membership_fee_id
            ) paid_per_fee
                ON  paid_per_fee.member_id        = ms.member_id
                AND paid_per_fee.membership_fee_id = f.id
            WHERE ms.end_date IS NULL
            GROUP BY ms.collectivity_id, ms.member_id
        """);
            upToDatePs.setDate(1, Date.valueOf(from));
            upToDatePs.setDate(2, Date.valueOf(to));

            ResultSet upToDateRs = upToDatePs.executeQuery();
            Map<String, Integer> upToDateCountByCollectivity = new HashMap<>();
            while (upToDateRs.next()) {
                String colId = upToDateRs.getString("collectivity_id");
                boolean isUpToDate = upToDateRs.getBoolean("is_up_to_date");
                if (isUpToDate) {
                    upToDateCountByCollectivity.merge(colId, 1, Integer::sum);
                }
            }

            PreparedStatement assiduityPs = connection.prepareStatement("""
            SELECT
                ms.collectivity_id,
                AVG(
                    CASE WHEN member_stats.total_required = 0
                         THEN 100.0
                         ELSE member_stats.attended * 100.0 / member_stats.total_required
                    END
                ) AS avg_assiduity
            FROM memberships ms
            JOIN (
                SELECT
                    a.collectivity_id,
                    arm.required_member         AS occupation,
                    COUNT(a.id)                 AS total_required,
                    COUNT(aa.id) FILTER (WHERE aa.status = 'ATTENDED') AS attended,
                    aa.member_id
                FROM activities a
                JOIN activity_required_members arm ON arm.activity_id = a.id
                LEFT JOIN activity_attendances aa ON aa.activity_id = a.id
                WHERE a.executive_date BETWEEN ? AND ?
                GROUP BY a.collectivity_id, arm.required_member, aa.member_id
            ) member_stats
                ON  member_stats.collectivity_id = ms.collectivity_id
                AND member_stats.occupation      = ms.occupation
                AND member_stats.member_id       = ms.member_id
            WHERE ms.end_date IS NULL
            GROUP BY ms.collectivity_id
        """);
            assiduityPs.setDate(1, Date.valueOf(from));
            assiduityPs.setDate(2, Date.valueOf(to));

            ResultSet assiduityRs = assiduityPs.executeQuery();
            Map<String, Double> assiduityByCollectivity = new HashMap<>();
            while (assiduityRs.next()) {
                assiduityByCollectivity.put(
                        assiduityRs.getString("collectivity_id"),
                        assiduityRs.getDouble("avg_assiduity")
                );
            }

            for (Object[] row : collectivitiesData) {
                String colId      = (String)  row[0];
                String name       = (String)  row[1];
                int    number     = (Integer) row[2];
                int    total      = (Integer) row[3];
                int    newMembers = (Integer) row[4];

                int upToDate = upToDateCountByCollectivity.getOrDefault(colId, 0);
                double percentage = (total == 0) ? 0.0 : (upToDate * 100.0 / total);
                double assiduity  = assiduityByCollectivity.getOrDefault(colId, 0.0);

                result.add(new CollectivityOverallStatistics(
                        new CollectivityInformation(name, number),
                        newMembers,
                        percentage,
                        assiduity
                ));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}

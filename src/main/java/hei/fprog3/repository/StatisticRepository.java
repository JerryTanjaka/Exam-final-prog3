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
    public List<MemberStatistic> getCollectivityMemberStatistic(String collectivityId,
                                                                LocalDate from,
                                                                LocalDate to) {
        Connection connection = dataSource.getConnection();
        try {

            PreparedStatement memberPs = connection.prepareStatement("""
                SELECT DISTINCT ON (m.id)
                       m.id, m.first_name, m.last_name, m.email, ms.occupation
                FROM members AS m
                JOIN memberships AS ms ON m.id = ms.member_id
                WHERE ms.collectivity_id = ?
                  AND ms.end_date IS NULL
                ORDER BY m.id, ms.start_date DESC
                """);
            memberPs.setString(1, collectivityId);

            PreparedStatement amountPs = connection.prepareStatement("""
                WITH total_earned AS (
                    SELECT COALESCE(SUM(p.amount), 0) AS earnedAmount
                    FROM payments AS p
                    JOIN fees AS f ON f.id = p.membership_fee_id
                    WHERE f.collectivity_id = ?
                      AND p.member_id = ?
                      AND p.creation_date BETWEEN ? AND ?
                ), total_active_fees AS (
                    SELECT COALESCE(SUM(f.amount), 0) AS total_fees
                    FROM fees AS f
                    WHERE f.collectivity_id = ?
                      AND f.status = 'ACTIVE'
                      AND f.eligible_from BETWEEN ? AND ?
                )
                SELECT earnedAmount,
                       GREATEST(0, total_fees - earnedAmount) AS dueAmount
                FROM total_earned, total_active_fees
                """);
            amountPs.setString(1, collectivityId);
            amountPs.setDate(3, Date.valueOf(from));
            amountPs.setDate(4, Date.valueOf(to));
            amountPs.setString(5, collectivityId);
            amountPs.setDate(6, Date.valueOf(from));
            amountPs.setDate(7, Date.valueOf(to));

            PreparedStatement assiduityPs = connection.prepareStatement("""
                SELECT
                    COUNT(DISTINCT a.id)                                              AS total_required,
                    COUNT(aa.id) FILTER (WHERE aa.status = 'ATTENDED')                AS attended
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
                String memberId   = memberRs.getString("id");
                String occupation = memberRs.getString("occupation");

                amountPs.setString(2, memberId);
                ResultSet amountRs = amountPs.executeQuery();
                if (!amountRs.next()) throw new RuntimeException("Empty amountRs for member " + memberId);

                assiduityPs.setString(1, occupation);
                assiduityPs.setString(2, memberId);
                ResultSet assiduityRs = assiduityPs.executeQuery();

                double assiduityPercentage = 100.0;
                if (assiduityRs.next()) {
                    int total    = assiduityRs.getInt("total_required");
                    int attended = assiduityRs.getInt("attended");
                    if (total > 0) {
                        assiduityPercentage = attended * 100.0 / total;
                    }
                }

                MemberDescription memberDescription = new MemberDescription(
                        memberId,
                        memberRs.getString("first_name"),
                        memberRs.getString("last_name"),
                        memberRs.getString("email"),
                        PositionType.valueOf(occupation)
                );

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
            PreparedStatement collectivitiesPs = connection.prepareStatement("""
                SELECT c.id, c.name, c.number,
                       COUNT(ms.member_id)                                               AS total_members,
                       COUNT(ms.member_id) FILTER (WHERE ms.start_date BETWEEN ? AND ?) AS new_members
                FROM collectivities c
                LEFT JOIN memberships ms
                    ON ms.collectivity_id = c.id
                    AND ms.end_date IS NULL
                GROUP BY c.id, c.name, c.number
                ORDER BY c.name
                """);
            collectivitiesPs.setDate(1, Date.valueOf(from));
            collectivitiesPs.setDate(2, Date.valueOf(to));

            ResultSet collectivitiesRs = collectivitiesPs.executeQuery();
            List<Object[]> collectivitiesData = new ArrayList<>();
            List<String>   collectivityIds    = new ArrayList<>();

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
                        COALESCE(paid_per_fee.paid_amount, 0) >= f.amount
                    ) AS is_up_to_date
                FROM memberships ms
                JOIN fees f
                    ON f.collectivity_id = ms.collectivity_id
                    AND f.status = 'ACTIVE'
                LEFT JOIN (
                    SELECT p.member_id,
                           p.membership_fee_id,
                           SUM(p.amount) AS paid_amount
                    FROM payments p
                    WHERE p.creation_date BETWEEN ? AND ?
                    GROUP BY p.member_id, p.membership_fee_id
                ) paid_per_fee
                    ON  paid_per_fee.member_id         = ms.member_id
                    AND paid_per_fee.membership_fee_id = f.id
                WHERE ms.end_date IS NULL
                GROUP BY ms.collectivity_id, ms.member_id
                """);
            upToDatePs.setDate(1, Date.valueOf(from));
            upToDatePs.setDate(2, Date.valueOf(to));

            ResultSet upToDateRs = upToDatePs.executeQuery();
            Map<String, Integer> upToDateCountByCollectivity = new HashMap<>();
            while (upToDateRs.next()) {
                String  colId      = upToDateRs.getString("collectivity_id");
                boolean isUpToDate = upToDateRs.getBoolean("is_up_to_date");
                if (isUpToDate) {
                    upToDateCountByCollectivity.merge(colId, 1, Integer::sum);
                }
            }

            PreparedStatement assiduityPs = connection.prepareStatement("""
                SELECT
                    ms.collectivity_id,
                    ms.member_id,
                    COUNT(DISTINCT a.id)                                               AS total_required,
                    COUNT(aa.id) FILTER (WHERE aa.status = 'ATTENDED')                 AS attended
                FROM memberships ms
                JOIN activities a
                    ON a.collectivity_id = ms.collectivity_id
                    AND a.executive_date BETWEEN ? AND ?
                JOIN activity_required_members arm
                    ON arm.activity_id    = a.id
                    AND arm.required_member = ms.occupation::position_type
                LEFT JOIN activity_attendances aa
                    ON aa.activity_id = a.id
                    AND aa.member_id  = ms.member_id
                WHERE ms.end_date IS NULL
                GROUP BY ms.collectivity_id, ms.member_id
                """);
            assiduityPs.setDate(1, Date.valueOf(from));
            assiduityPs.setDate(2, Date.valueOf(to));

            Map<String, List<Double>> assiduityListByCollectivity = new HashMap<>();
            ResultSet assiduityRs = assiduityPs.executeQuery();
            while (assiduityRs.next()) {
                String colId   = assiduityRs.getString("collectivity_id");
                int    total   = assiduityRs.getInt("total_required");
                int    attended = assiduityRs.getInt("attended");
                double pct = (total == 0) ? 100.0 : (attended * 100.0 / total);
                assiduityListByCollectivity.computeIfAbsent(colId, k -> new ArrayList<>()).add(pct);
            }

            List<CollectivityOverallStatistics> result = new ArrayList<>();
            for (Object[] row : collectivitiesData) {
                String colId      = (String)  row[0];
                String name       = (String)  row[1];
                int    number     = (Integer) row[2];
                int    total      = (Integer) row[3];
                int    newMembers = (Integer) row[4];

                int    upToDate   = upToDateCountByCollectivity.getOrDefault(colId, 0);
                double duePct     = (total == 0) ? 0.0 : (upToDate * 100.0 / total);

                List<Double> assiduities = assiduityListByCollectivity.getOrDefault(colId, new ArrayList<>());

                double avgAssiduity;
                if (total == 0) {
                    avgAssiduity = 0.0;
                } else {
                    int membersWithNoRequiredActivity = total - assiduities.size();
                    double sumAssiduity = assiduities.stream().mapToDouble(Double::doubleValue).sum()
                            + (membersWithNoRequiredActivity * 100.0);
                    avgAssiduity = sumAssiduity / total;
                }

                result.add(new CollectivityOverallStatistics(
                        new CollectivityInformation(name, number),
                        newMembers,
                        duePct,
                        avgAssiduity
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
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
import java.util.List;

@Repository
public class StatisticRepository {
    private DataSourceConfig dataSource;
    public StatisticRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    public List<MemberStatistic> getCollectivityMemberStatistic(String collectivityId, LocalDate from, LocalDate to) throws NotFoundException {
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
                            JOIN transactions AS t ON p.id = t.payment_id
                        WHERE f.collectivity_id = ? AND t.member_id = ?
                            AND t.creation_date BETWEEN ? AND ?
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

            ResultSet memberRs = memberPs.executeQuery();
            List<MemberStatistic> memberStatistics = new ArrayList<>();
            while(memberRs.next()) {
                amountPs.setString(2, memberRs.getString("id"));
                ResultSet amountRs = amountPs.executeQuery();
                if (!amountRs.next()) {
                    throw new RuntimeException("Empty amountRs");
                }
                MemberDescription memberDescription = new MemberDescription();
                memberDescription.setId(memberRs.getString("id"));
                memberDescription.setFirstName(memberRs.getString("first_name"));
                memberDescription.setLastName(memberRs.getString("last_name"));
                memberDescription.setEmail(memberRs.getString("email"));
                memberDescription.setOccupation(PositionType.valueOf(memberRs.getString("occupation")));
                memberStatistics.add(new MemberStatistic(memberDescription, amountRs.getDouble("earnedAmount"), amountRs.getDouble("dueAmount")));
            }
            return memberStatistics;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
// Remplacement complet de la méthode getOverallStatistics dans StatisticRepository.java

    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("""
            WITH member_status AS (
                SELECT
                    ms.collectivity_id,
                    ms.member_id,
                    ms.start_date,
                    CASE WHEN NOT EXISTS (
                        -- Existe-t-il un paiement lié à cette collectivité
                        -- que ce membre n'a pas couvert ?
                        SELECT 1 FROM fees f
                        JOIN accounts a ON a.collectivity_id = ms.collectivity_id
                        WHERE f.collectivity_id = ms.collectivity_id
                          AND f.status = 'ACTIVE'
                          AND (
                              SELECT COALESCE(SUM(p.amount), 0)
                              FROM payments p
                              JOIN transactions t ON t.payment_id = p.id
                              WHERE t.member_id = ms.member_id
                                AND p.membership_fee_id = f.id
                                AND p.credited_account_id = a.id
                                AND p.creation_date BETWEEN ? AND ?
                          ) < f.amount
                    ) THEN 1 ELSE 0 END AS is_up_to_date
                FROM memberships ms
                WHERE ms.end_date IS NULL
            )
            SELECT
                c.id,
                c.name,
                c.number,
                COUNT(mst.member_id)                                                   AS total_members,
                SUM(mst.is_up_to_date)                                                 AS up_to_date_count,
                COUNT(mst.member_id) FILTER (WHERE mst.start_date BETWEEN ? AND ?)     AS new_members
            FROM collectivities c
            JOIN member_status mst ON mst.collectivity_id = c.id
            GROUP BY c.id, c.name, c.number
            ORDER BY c.name
        """);

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();
            List<CollectivityOverallStatistics> result = new ArrayList<>();

            while (rs.next()) {
                int total = rs.getInt("total_members");
                int upToDate = rs.getInt("up_to_date_count");
                double percentage = (total == 0) ? 0.0 : (upToDate * 100.0 / total);

                CollectivityInformation info = new CollectivityInformation(
                        rs.getString("name"),
                        rs.getInt("number")
                );

                result.add(new CollectivityOverallStatistics(
                        info,
                        rs.getInt("new_members"),
                        percentage
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

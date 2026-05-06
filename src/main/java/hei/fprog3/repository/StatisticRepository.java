package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.member.MemberDescription;
import hei.fprog3.dto.statistic.MemberStatistic;
import hei.fprog3.model.enums.PositionType;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticRepository {
    private DataSourceConfig dataSource;
    public StatisticRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    public List<MemberStatistic> getCollectivityMemberStatistic(String collectivityId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                """
                SELECT m.id, m.first_name, m.last_name, m.email, SUM(p.amount) AS earnedAmount FROM members AS m
                JOIN memberships AS ms ON ms.collectivity_id = ?
                JOIN fees AS f ON f.collectivity_id = ?
                JOIN payments AS p ON p.membership_fee_id = f.id
                JOIN transactions AS t ON t.payment_id = p.id
                WHERE ms.collectivity_id = ?
                GROUP BY m.id, ms.start_date
                ORDER BY ms.start_date DESC
                """);

            ps.setString(1, collectivityId);
            ps.setString(2, collectivityId);
            ps.setString(3, collectivityId);

            ResultSet rs = ps.executeQuery();
            List<MemberStatistic> memberStatistics = new ArrayList<>();
            while(rs.next()) {
                MemberDescription memberDescription = new MemberDescription();
                memberDescription.setId(rs.getString("id"));
                memberDescription.setFirstName(rs.getString("first_name"));
                memberDescription.setLastName(rs.getString("last_name"));
                memberDescription.setEmail(rs.getString("email"));
                memberDescription.setOccupation(null);
                memberStatistics.add(new MemberStatistic(memberDescription, rs.getDouble("earnedAmount"), 0));
            }
            return memberStatistics;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}

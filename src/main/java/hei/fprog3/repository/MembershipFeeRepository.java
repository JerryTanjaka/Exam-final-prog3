package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.fees.CreateMembershipFee;
import hei.fprog3.dto.fees.MembershipFeeResponse;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MembershipFeeRepository {
    private final DataSourceConfig dataSource;

    public MembershipFeeRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    public List<MembershipFeeResponse> findByCollectivityId(String collectivityId) {
        List<MembershipFeeResponse> fees = new ArrayList<>();
        String sql = "SELECT id, label, amount, fee_frequency, eligible_from, status FROM membership_fees WHERE collectivity_id = ?::UUID";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MembershipFeeResponse fee = new MembershipFeeResponse();
                fee.setId(rs.getString("id"));
                fee.setLabel(rs.getString("label"));
                fee.setAmount(rs.getDouble("amount"));
                fee.setFrequency(rs.getString("fee_frequency"));
                fee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                fee.setStatus(rs.getString("status"));
                fees.add(fee);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la récupération des frais", e);
        }
        return fees;
    }

    public List<MembershipFeeResponse> saveAll(String collectivityId, List<CreateMembershipFee> fees) {
        String sql = "INSERT INTO membership_fees (collectivity_id, label, amount, fee_frequency, eligible_from) VALUES (?::UUID, ?, ?, ?::frequency, ?)";

        Connection conn = dataSource.getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (CreateMembershipFee fee : fees) {
                    ps.setString(1, collectivityId);
                    ps.setString(2, fee.getLabel());
                    ps.setDouble(3, fee.getAmount());
                    ps.setString(4, fee.getFrequency());
                    ps.setDate(5, Date.valueOf(fee.getEligibleFrom()));
                    ps.addBatch();
                }

                ps.executeBatch();
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de l'insertion des frais", e);
        } finally {
            dataSource.closeConnection(conn);
        }

        return findByCollectivityId(collectivityId);
    }
}
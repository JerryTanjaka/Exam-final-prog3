package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.collectivity.CollectivityResponse;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.dto.fee.FeeRequest;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Fee;
import hei.fprog3.model.enums.FeeFrequencyType;
import hei.fprog3.model.enums.PositionType;
import hei.fprog3.model.enums.StatusType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FeeRepository {
    private final CollectivityRepository collectivityRepository;
    private DataSourceConfig dataSource;
    public  FeeRepository(DataSourceConfig dataSource, CollectivityRepository collectivityRepository) {
        this.dataSource = dataSource;
        this.collectivityRepository = collectivityRepository;
    }

    public List<Fee> getAllCollectivityFees(String id) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            collectivityRepository.exists(id);

            PreparedStatement ps = connection.prepareStatement(
                """
                SELECT f.id, eligible_from, amount, label, frequency, status
                FROM fees AS f JOIN collectivityfee AS cf ON cf.fee_id = f.id
                WHERE cf.collectivity_id = ?::UUID
                """);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            List<Fee> collectivityFees = new ArrayList<>();
            while (rs.next()) {
                Fee fee = new Fee();
                fee.setId(rs.getString("id"));
                fee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                fee.setAmount(rs.getDouble("amount"));
                fee.setLabel(rs.getString("label"));
                fee.setStatus(StatusType.valueOf(rs.getString("status")));
                fee.setFrequency(FeeFrequencyType.valueOf(rs.getString("frequency")));
                collectivityFees.add(fee);
            }
            return collectivityFees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Fee> create(String collectivityId, List<FeeRequest> feeRequests) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            collectivityRepository.exists(collectivityId);

            connection.setAutoCommit(false);
            List<String> newFeesid = new ArrayList<>();
            for (FeeRequest feeRequest : feeRequests) {
                String newFeeId = UUID.randomUUID().toString();
                newFeesid.add(newFeeId);
                PreparedStatement feesPs = connection.prepareStatement(
                        """
                        INSERT INTO fees (id, eligible_from, amount, label, frequency, status)
                        VALUES (?::UUID, ?, ?::FLOAT, ?, ?::fee_frequency_type, ?::activity_status)
                        """
                );
                feesPs.setString(1, newFeeId);
                feesPs.setDate(2, Date.valueOf(feeRequest.getEligibleFrom()));
                feesPs.setDouble(3, feeRequest.getAmount());
                feesPs.setString(4, feeRequest.getLabel());
                feesPs.setString(5, feeRequest.getFrequency().name());
                feesPs.setString(6, ((feeRequest.getEligibleFrom() == null || feeRequest.getEligibleFrom().isAfter(LocalDate.now())) ? StatusType.INACTIVE.name() : StatusType.ACTIVE.name()));
                feesPs.execute();

                PreparedStatement collectivityFeePs = connection.prepareStatement(
                        """
                        INSERT INTO collectivityfee (collectivity_id, fee_id)
                        VALUES (?::UUID, ?::UUID)
                        """
                );
                collectivityFeePs.setString(1, collectivityId);
                collectivityFeePs.setString(2, newFeeId);
                collectivityFeePs.execute();

            }
            connection.commit();
            List<Fee> newFees = new ArrayList<>();
            for (String id : newFeesid) {
                newFees.add(this.findById(id));
            }
            return newFees;
        } catch (SQLException e) {
            dataSource.rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public Fee findById(String id) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    """
                    SELECT f.id, eligible_from, amount, label, frequency, status
                    FROM fees AS f
                    WHERE f.id = ?::UUID
                    """);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Fee fee = new Fee();
                fee.setId(rs.getString("id"));
                fee.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                fee.setAmount(rs.getDouble("amount"));
                fee.setLabel(rs.getString("label"));
                fee.setStatus(StatusType.valueOf(rs.getString("status")));
                fee.setFrequency(FeeFrequencyType.valueOf(rs.getString("frequency")));
                return fee;
            }
            throw new NotFoundException("Fee with id %s not found".formatted(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}

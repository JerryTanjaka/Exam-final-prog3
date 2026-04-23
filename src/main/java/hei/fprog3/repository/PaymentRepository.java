package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.payment.PaymentRequest;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.FinancialAccount;
import hei.fprog3.model.Payment;
import hei.fprog3.model.enums.PaymentMethod;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class PaymentRepository {
    private final MemberRepository memberRepository;
    private DataSourceConfig dataSource;
    private AccountRepository accountRepository;

    public PaymentRepository(DataSourceConfig dataSource, AccountRepository accountRepository, MemberRepository memberRepository) {
        this.dataSource = dataSource;
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
    }

    public List<Payment> create(List<PaymentRequest> paymentRequests) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            for (PaymentRequest request : paymentRequests) {
                memberRepository.findById(request.getPayerId());
            }
            connection.setAutoCommit(false);
            List<UUID> generatedPaymentIds = new ArrayList<>();

            PreparedStatement paymentPs = connection.prepareStatement(
                    """
                    INSERT INTO payments (id, membership_fee_id, credited_account_id, amount, payment_method)
                    VALUES (?, ?, ?, ?::FLOAT, ?::payment_method);
                    """
            );

            PreparedStatement transactionPs = connection.prepareStatement(
                        """
                        INSERT INTO transactions (member_id, payment_id)
                        VALUES (?, ?);
                        """
            );

            PreparedStatement accountsPs = connection.prepareStatement(
                    """
                    UPDATE accounts SET balance = balance + ?::FLOAT WHERE id = ?
                    """
            );

            for (PaymentRequest paymentRequest : paymentRequests) {
                UUID newPaymentId = UUID.randomUUID();
                generatedPaymentIds.add(newPaymentId);

                paymentPs.setString(1, newPaymentId.toString());
                paymentPs.setString(2, paymentRequest.getMembershipFeeIdentifier());
                paymentPs.setString(3, paymentRequest.getAccountCreditedIdentifier());
                paymentPs.setDouble(4, paymentRequest.getAmount());
                paymentPs.setString(5, paymentRequest.getPaymentMode().name());
                paymentPs.addBatch();

                transactionPs.setString(1, paymentRequest.getPayerId());
                transactionPs.setString(2, newPaymentId.toString());
                transactionPs.addBatch();

                accountsPs.setDouble(1, paymentRequest.getAmount());
                accountsPs.setString(2, paymentRequest.getAccountCreditedIdentifier());
                accountsPs.addBatch();
            }

            paymentPs.executeBatch();
            transactionPs.executeBatch();
            accountsPs.executeBatch();

            connection.commit();
            List<Payment> payments = new ArrayList<>();
            for (UUID paymentId : generatedPaymentIds) {
                payments.add(this.findById(paymentId.toString()));
            }
            return payments;
        } catch (SQLException e) {
            dataSource.rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public Payment findById(String paymentId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement paymentsPs = connection.prepareStatement(
                        """
                        SELECT p.id, amount, payment_method, creation_date, accounts.id AS account_id
                        FROM payments AS p
                        JOIN accounts ON p.credited_account_id = accounts.id
                        WHERE p.id = ?
                        """);
            paymentsPs.setString(1, paymentId);
            ResultSet rs = paymentsPs.executeQuery();

            if (!rs.next()) {
                return null;
            }

            FinancialAccount account = accountRepository.findById(rs.getString("account_id"));

            Payment payment = new Payment();
            payment.setId(rs.getString("id"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setCreationDate(rs.getDate("creation_date").toLocalDate());
            payment.setPaymentMode(PaymentMethod.valueOf(rs.getString("payment_method")));
            payment.setAccountCredited(account);
            return payment;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}

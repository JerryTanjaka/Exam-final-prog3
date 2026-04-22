package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.payment.PaymentRequest;
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
    private DataSourceConfig dataSource;
    private AccountRepository accountRepository;

    public PaymentRepository(DataSourceConfig dataSource, AccountRepository accountRepository) {
        this.dataSource = dataSource;
        this.accountRepository = accountRepository;
    }

    public List<Payment> create(List<PaymentRequest> paymentRequests) {
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            List<UUID> generatedPaymentIds = new ArrayList<>();
            for (PaymentRequest paymentRequest : paymentRequests) {
                UUID newPaymentId = UUID.randomUUID();
                generatedPaymentIds.add(newPaymentId);

                PreparedStatement paymentPs = connection.prepareStatement(
                        """
                        INSERT INTO payments (id, membership_fee_id, credited_account_id, amount, payment_method)
                        VALUES (?::UUID, ?::UUID, ?::UUID, ?::FLOAT, ?::payment_method);
                        """
                );
                paymentPs.setString(1, newPaymentId.toString());
                paymentPs.setString(2, paymentRequest.getMembershipFeeIdentifier());
                paymentPs.setString(3, paymentRequest.getAccountCreditedIdentifier());
                paymentPs.setDouble(4, paymentRequest.getAmount());
                paymentPs.setString(5, paymentRequest.getPaymentMode().name());
                paymentPs.executeUpdate();

                PreparedStatement transactionPs = connection.prepareStatement("""
                        INSERT INTO transactions (member_id, payment_id)
                        VALUES (?::UUID, ?::UUID);
                        """);
                transactionPs.setString(1, paymentRequest.getPayerId());
                transactionPs.setString(2, newPaymentId.toString());
                transactionPs.executeUpdate();

                PreparedStatement accountsPs = connection.prepareStatement(
                        """
                        UPDATE accounts SET balance = balance + ?::FLOAT WHERE id = ?::UUID
                        """);
                accountsPs.setDouble(1, paymentRequest.getAmount());
                accountsPs.setString(2, paymentRequest.getAccountCreditedIdentifier());
                accountsPs.executeUpdate();

            }
            connection.commit();
            List<Payment> payments = new ArrayList<>();
            for (UUID paymentId : generatedPaymentIds) {
                payments.add(this.findById(paymentId.toString()));
            }
            return payments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Payment findById(String paymentId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement paymentsPs = connection.prepareStatement(
                        """
                        SELECT id, amount, payment_method, creation_date, accounts.id AS account_id
                        FROM payments AS p
                        JOIN accounts ON payments.credited_account_id = account_id
                        WHERE id = ?::UUID
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
        }
    }
}

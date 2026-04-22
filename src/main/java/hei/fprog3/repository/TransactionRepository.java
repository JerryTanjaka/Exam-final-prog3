package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Payment;
import hei.fprog3.model.Transaction;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {
    private final CollectivityRepository collectivityRepository;
    private DataSourceConfig dataSource;
    private PaymentRepository paymentRepository;
    private MemberRepository memberRepository;

    public TransactionRepository(DataSourceConfig dataSource, PaymentRepository paymentRepository, MemberRepository memberRepository, CollectivityRepository collectivityRepository) {
        this.dataSource = dataSource;
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
    }


    public List<Transaction> getTransactionBetween(String collectivityId, LocalDate from, LocalDate to) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            collectivityRepository.exists(collectivityId);
            List<Transaction> transactions = new ArrayList<>();
            PreparedStatement transactionsPs = connection.prepareStatement(
                    """
                    SELECT t.id, member_id, payment_id, t.creation_date
                    FROM transactions AS t
                    JOIN payments AS p ON payment_id = p.id
                    JOIN accounts AS a ON a.id = p.credited_account_id
                    WHERE a.collectivity_id = ?::UUID AND t.creation_date BETWEEN ? AND ?
                    """);
            transactionsPs.setString(1, collectivityId);
            transactionsPs.setDate(2, Date.valueOf(from));
            transactionsPs.setDate(3, Date.valueOf(to));
            ResultSet rs = transactionsPs.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                Payment payment = paymentRepository.findById(rs.getString("payment_id"));
                transaction.setId(rs.getString("id"));
                transaction.setCreationDate(rs.getDate("creation_date").toLocalDate());
                transaction.setAmount(payment.getAmount());
                transaction.setPaymentMode(payment.getPaymentMode());
                transaction.setAccountCredited(payment.getAccountCredited());
                transaction.setMemberDebited(memberRepository.findById(rs.getString("member_id")));
                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}

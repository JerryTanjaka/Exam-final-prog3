package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.FinancialAccount;
import hei.fprog3.model.Payment;
import hei.fprog3.model.Transaction;
import hei.fprog3.model.enums.PaymentMethod;
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
    private AccountRepository accountRepository;

    public TransactionRepository(DataSourceConfig dataSource,
                                 PaymentRepository paymentRepository,
                                 MemberRepository memberRepository,
                                 CollectivityRepository collectivityRepository,
                                 AccountRepository accountRepository) {
        this.dataSource = dataSource;
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transaction> getTransactionBetween(String collectivityId,
                                                   LocalDate from,
                                                   LocalDate to) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            collectivityRepository.exists(collectivityId);

            List<Transaction> transactions = new ArrayList<>();

            PreparedStatement transactionsPs = connection.prepareStatement("""
                SELECT
                    p.id           AS payment_id,
                    p.member_id,
                    p.amount,
                    p.payment_method,
                    p.creation_date,
                    p.credited_account_id
                FROM payments AS p
                JOIN accounts AS a ON a.id = p.credited_account_id
                WHERE a.collectivity_id = ?
                  AND p.creation_date BETWEEN ? AND ?
                ORDER BY p.creation_date ASC
                """);
            transactionsPs.setString(1, collectivityId);
            transactionsPs.setDate(2, Date.valueOf(from));
            transactionsPs.setDate(3, Date.valueOf(to));

            ResultSet rs = transactionsPs.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getString("payment_id"));
                transaction.setCreationDate(rs.getDate("creation_date").toLocalDate());
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setPaymentMode(PaymentMethod.valueOf(rs.getString("payment_method")));

                FinancialAccount account = accountRepository.findById(rs.getString("credited_account_id"));
                transaction.setAccountCredited(account);
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
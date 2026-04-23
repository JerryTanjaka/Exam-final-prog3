package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.model.BankAccount;
import hei.fprog3.model.CashAccount;
import hei.fprog3.model.FinancialAccount;
import hei.fprog3.model.MobileBankingAccount;
import hei.fprog3.model.enums.AccountType;
import hei.fprog3.model.enums.BankType;
import hei.fprog3.model.enums.MobileBankingServiceType;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository {
    private DataSourceConfig dataSource;
    public AccountRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    public FinancialAccount findById(String id) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement accountsPs = connection.prepareStatement(
                """
                SELECT id, type ,balance, holder_name, bank_name, bank_account_number, mobile_banking_service, mobile_number
                FROM accounts WHERE id = ?
                """);
            accountsPs.setString(1, id);
            ResultSet rs = accountsPs.executeQuery();
            if (rs.next()) {
                if (AccountType.valueOf(rs.getString("type")).equals(AccountType.CASH)) {
                    CashAccount account = new CashAccount();
                    account.setId(rs.getString("id"));
                    account.setAmount(rs.getDouble("balance"));
                    return account;
                } else if (AccountType.valueOf(rs.getString("type")).equals(AccountType.BANK)) {
                    BankAccount account = new BankAccount();
                    account.setId(rs.getString("id"));
                    account.setAmount(rs.getDouble("balance"));
                    account.setBankName(BankType.valueOf(rs.getString("bank_name")));
                    account.setAccountNumberFieldsFromFullNumber(rs.getString("bank_account_number"));
                    account.setHolderName(rs.getString("holder_name"));
                    return account;
                } else if (AccountType.valueOf(rs.getString("type")).equals(AccountType.MOBILE_MONEY)) {
                    MobileBankingAccount account = new MobileBankingAccount();
                    account.setId(rs.getString("id"));
                    account.setAmount(rs.getDouble("balance"));
                    account.setHolderName(rs.getString("holder_name"));
                    account.setMobileNumber(rs.getString("mobile_number"));
                    account.setMobileBankingService(MobileBankingServiceType.valueOf(rs.getString("mobile_banking_service")));
                    return account;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }

    }
    public List<FinancialAccount> findByCollectivityId(String collectivityId, LocalDate at) {
        Connection connection = dataSource.getConnection();
        try {
            String query;
            if (at != null) {
                query = """
                SELECT a.id, a.type, a.holder_name, a.bank_name, a.bank_account_number,
                       a.mobile_banking_service, a.mobile_number,
                       COALESCE(SUM(p.amount), 0) AS balance
                FROM accounts a
                LEFT JOIN payments p
                    ON p.credited_account_id = a.id
                    AND p.creation_date <= ?
                WHERE a.collectivity_id = ?
                GROUP BY a.id, a.type, a.holder_name, a.bank_name,
                         a.bank_account_number, a.mobile_banking_service, a.mobile_number
                """;
            } else {
                query = """
                SELECT id, type, balance, holder_name, bank_name, bank_account_number,
                       mobile_banking_service, mobile_number
                FROM accounts
                WHERE collectivity_id = ?
                """;
            }

            PreparedStatement ps = connection.prepareStatement(query);
            if (at != null) {
                ps.setDate(1, Date.valueOf(at));
                ps.setString(2, collectivityId);
            } else {
                ps.setString(1, collectivityId);
            }

            ResultSet rs = ps.executeQuery();
            List<FinancialAccount> accounts = new ArrayList<>();

            while (rs.next()) {
                AccountType type = AccountType.valueOf(rs.getString("type"));
                if (type == AccountType.CASH) {
                    CashAccount account = new CashAccount();
                    account.setId(rs.getString("id"));
                    account.setAmount(rs.getDouble("balance"));
                    accounts.add(account);
                } else if (type == AccountType.BANK) {
                    BankAccount account = new BankAccount();
                    account.setId(rs.getString("id"));
                    account.setAmount(rs.getDouble("balance"));
                    account.setHolderName(rs.getString("holder_name"));
                    account.setBankName(BankType.valueOf(rs.getString("bank_name")));
                    account.setAccountNumberFieldsFromFullNumber(rs.getString("bank_account_number"));
                    accounts.add(account);
                } else if (type == AccountType.MOBILE_MONEY) {
                    MobileBankingAccount account = new MobileBankingAccount();
                    account.setId(rs.getString("id"));
                    account.setAmount(rs.getDouble("balance"));
                    account.setHolderName(rs.getString("holder_name"));
                    account.setMobileNumber(rs.getString("mobile_number"));
                    account.setMobileBankingService(
                            MobileBankingServiceType.valueOf(rs.getString("mobile_banking_service"))
                    );
                    accounts.add(account);
                }
            }
            return accounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}

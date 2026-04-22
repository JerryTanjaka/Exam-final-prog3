package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.model.BankAccount;
import hei.fprog3.model.CashAccount;
import hei.fprog3.model.FinancialAccount;
import hei.fprog3.model.MobileBankingAccount;
import hei.fprog3.model.enums.AccountType;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                FROM accounts WHERE id = ?::UUID
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
                    account.setAccountNumberFieldsFromFullNumber(rs.getString("bank_account_number"));
                    account.setHolderName(rs.getString("holder_name"));
                    return account;
                } else if (AccountType.valueOf(rs.getString("type")).equals(AccountType.MOBILE_MONEY)) {
                    MobileBankingAccount account = new MobileBankingAccount();
                    account.setId(rs.getString("id"));
                    account.setAmount(rs.getDouble("balance"));
                    account.setHolderName(rs.getString("holder_name"));
                    account.setMobileNumber(account.getMobileNumber());
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

package hei.fprog3.model;

import hei.fprog3.model.enums.BankType;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount extends FinancialAccount {
    private String holderName;
    private BankType bankName;
    private String bankCode;
    private String bankBranchCode;
    private String bankAccountNumber;
    private String bankAccountKey;

    public String getFullAccountNumber() {
        return bankCode + bankBranchCode + bankAccountNumber + bankAccountKey;
    }

    public void setAccountNumberFieldsFromFullNumber(String fullAccountNumber) {
        if  (fullAccountNumber == null) {
            return;
        }
        if (fullAccountNumber.length() != 23) {
            return;
        }
        bankCode = fullAccountNumber.substring(0, 5);
        bankBranchCode = fullAccountNumber.substring(5, 10);
        bankAccountNumber = fullAccountNumber.substring(10, 21);
        bankAccountKey = fullAccountNumber.substring(21);
    }
}

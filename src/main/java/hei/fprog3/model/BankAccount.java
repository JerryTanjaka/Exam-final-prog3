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
}

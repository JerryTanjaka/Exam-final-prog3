package hei.fprog3.model;

import hei.fprog3.model.enums.MobileBankingServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MobileBankingAccount extends FinancialAccount {
    private String holderName;
    private MobileBankingServiceType MobileBankingService;
    private String mobileNumber;
}

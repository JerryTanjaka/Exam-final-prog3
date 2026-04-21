package hei.fprog3.model;

import hei.fprog3.model.enums.AccountType;
import hei.fprog3.model.enums.BankName;
import hei.fprog3.model.enums.MobileMoneyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Représente un compte financier (caisse, bancaire, mobile money).
 * Table : accounts
 *
 * Règles métier :
 * - communityId = null  ➜  compte de la fédération
 * - Une seule caisse (CASH) par entité (collectivité ou fédération)
 * - Plusieurs comptes BANK ou MOBILE_MONEY autorisés
 * - bankAccountNumber : exactement 23 chiffres (format RIB malgache)
 */

@Getter
@Setter
@AllArgsConstructor
public class Account {

    private int id;
    private int communityId;
    private AccountType type;
    private BigDecimal balance;
    private LocalDate statementDate;
    private LocalDate openingDate;

    private String holderName;
    private BankName bankName;
    private String bankAccountNumber;
    private MobileMoneyService mobileMoneyService;
    private String phoneNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account() {}

    /** Vérifie que le numéro RIB est bien composé de 23 chiffres */
    public boolean isBankAccountNumberValid() {
        return bankAccountNumber != null && bankAccountNumber.matches("\\d{23}");
    }

    /** Indique si ce compte appartient à la fédération (et non à une collectivité) */
    public boolean isFederationAccount() {
        return communityId == 0;
    }
}

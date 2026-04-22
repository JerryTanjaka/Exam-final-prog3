package hei.fprog3.model;

import hei.fprog3.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String id;
    double amount;
    private PaymentMethod paymentMode;
    private FinancialAccount accountCredited;
    private LocalDate creationDate;
}

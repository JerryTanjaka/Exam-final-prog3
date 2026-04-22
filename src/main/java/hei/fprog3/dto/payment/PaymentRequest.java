package hei.fprog3.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hei.fprog3.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @JsonIgnore
    private String payerId;
    private double amount;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private PaymentMethod paymentMode;
}

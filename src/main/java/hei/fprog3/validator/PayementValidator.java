package hei.fprog3.validator;

import hei.fprog3.dto.payment.PaymentRequest;
import hei.fprog3.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PayementValidator {
    public void validate(PaymentRequest payment) throws BadRequestException {
        List<String> errors = new ArrayList<>();
        if (payment.getAmount() <= 0) {
            errors.add("Amount must be greater than 0.");
        }
        if (payment.getAccountCreditedIdentifier() == null || payment.getAccountCreditedIdentifier().isEmpty()) {
            errors.add("Account Credited Identifier cannot be empty.");
        }
        if (payment.getMembershipFeeIdentifier() == null || payment.getMembershipFeeIdentifier().isEmpty()) {
            errors.add("Membership Fee Identifier cannot be empty.");
        }
        if (payment.getPaymentMode() == null) {
            errors.add("Payment Mode cannot be empty.");
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join("\n", errors));
        }
    }

    public void validate(List<PaymentRequest> payments) throws BadRequestException {
        if (payments == null || payments.isEmpty()) {
            throw new BadRequestException("Payment Request cannot be empty.");
        }
        for (PaymentRequest paymentRequest : payments) {
            validate(paymentRequest);
        }
    }
}

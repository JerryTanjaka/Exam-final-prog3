package hei.fprog3.validator;

import hei.fprog3.dto.fee.FeeRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.model.enums.FeeFrequencyType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FeeValidator {
    public void validate(FeeRequest feeRequest) throws BadRequestException {
        List<String> errors = new ArrayList<>();
        if (feeRequest.getAmount() <= 0) {
            errors.add("Amount must be greater than 0");
        }
        if (feeRequest.getLabel() ==  null || feeRequest.getLabel().isEmpty()) {
            errors.add("Label cannot be empty");
        }
        if (feeRequest.getEligibleFrom() == null) {
            errors.add("EligibleFrom cannot be null");
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join("\n", errors));
        }
    }

    public void validate(List<FeeRequest> feeRequests) throws BadRequestException {
        if (feeRequests == null || feeRequests.isEmpty()) {
            throw new BadRequestException("Fee Request cannot be empty.");
        }
        for (FeeRequest feeRequest : feeRequests) {
            validate(feeRequest);
        }
    }
}

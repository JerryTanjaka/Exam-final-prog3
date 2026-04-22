package hei.fprog3.validator;

import hei.fprog3.dto.fees.CreateMembershipFee;
import hei.fprog3.dto.financial.CreateMembershipFee;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.model.enums.Frequency; // À créer (voir étape 3)
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MembershipFeeValidator {

    public void validate(List<CreateMembershipFee> fees) throws BadRequestException {
        for (CreateMembershipFee fee : fees) {
            if (fee.getAmount() == null || fee.getAmount() < 0) {
                throw new BadRequestException("Amount cannot be under 0");
            }

            if (fee.getLabel() == null || fee.getLabel().isEmpty()) {
                throw new BadRequestException("Label is required");
            }

            // Vérifier si la fréquence est reconnue
            boolean isValidFrequency = Arrays.stream(Frequency.values())
                    .anyMatch(f -> f.name().equals(fee.getFrequency()));

            if (!isValidFrequency) {
                throw new BadRequestException("Unrecognized frequency: " + fee.getFrequency());
            }
        }
    }
}
package hei.fprog3.validator;

import hei.fprog3.dto.collectivity.CollectivityIdentity;
import hei.fprog3.dto.collectivity.CollectivityStructureRequest;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.model.Collectivity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CollectivityValidator {
    public void validate(CreateCollectivityRequest collectivity) throws BadRequestException {
        if (collectivity == null) {
            throw new BadRequestException("collectivity is null");
        }
        List<String> errors = new ArrayList<>();
        if (collectivity.getCity() == null ||  collectivity.getCity().isEmpty()) {
            errors.add("City");
        }
//        if (collectivity.getName() == null ||  collectivity.getName().isEmpty()) {
//            errors.add("Name");
//        }
        if (collectivity.getSpecialty() == null ||  collectivity.getSpecialty().isEmpty()) {
            errors.add("Specialty");
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join(", ", errors) + " are missing");
        }
        if (!collectivity.isFederationApproval()) {
            throw new BadRequestException("Not approved by federation");
        }
        if (collectivity.getMembers() == null ||  collectivity.getMembers().size() < 10) {
            throw new BadRequestException("Insufficient number of members");
        }
        CollectivityStructureRequest collectivityStructure = collectivity.getStructure();
        if (collectivityStructure == null ||
                (collectivityStructure.getPresident() == null ||  collectivityStructure.getPresident().isEmpty()) ||
                (collectivityStructure.getTreasurer() == null ||  collectivityStructure.getTreasurer().isEmpty()) ||
                (collectivityStructure.getSecretary() == null ||  collectivityStructure.getSecretary().isEmpty()) ||
                (collectivityStructure.getVicePresident() == null || collectivityStructure.getVicePresident().isEmpty())) {
            throw new BadRequestException("Structure is invalid");
        }
    }

    public void validate(List<CreateCollectivityRequest> collectivities) throws BadRequestException {
        for (CreateCollectivityRequest collectivity : collectivities) {
            this.validate(collectivity);
        }
    }

    public void validate(CollectivityIdentity collectivityIdentity) throws BadRequestException {
        if (collectivityIdentity == null) {
            throw new BadRequestException("collectivityIdentity is null");
        }
        if (collectivityIdentity.getName() == null ||  collectivityIdentity.getName().isEmpty()) {
            throw new BadRequestException("collectivityIdentity.getName() is null");
        }
        if (collectivityIdentity.getNumber() == null ||  collectivityIdentity.getNumber().isEmpty()) {
            throw new BadRequestException("collectivityIdentity.getNumber() is null");
        }
    }
}

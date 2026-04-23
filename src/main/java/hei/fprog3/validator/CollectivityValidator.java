package hei.fprog3.validator;

import hei.fprog3.dto.collectivity.CollectivityInformation;
import hei.fprog3.dto.collectivity.CollectivityStructureRequest;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.repository.CollectivityRepository;
import hei.fprog3.repository.MemberRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CollectivityValidator {
    private CollectivityRepository collectivityRepository;
    private MemberRepository memberRepository;
    public CollectivityValidator(MemberRepository memberRepository, CollectivityRepository collectivityRepository) {
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public void validate(CreateCollectivityRequest collectivity) throws BadRequestException {
        if (collectivity == null) {
            throw new BadRequestException("collectivity is null");
        }
        List<String> errors = new ArrayList<>();
        if (collectivity.getLocation() == null ||  collectivity.getLocation().isEmpty()) {
            errors.add("location");
        }
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
        int oldEnoughMember = 0;
        for (String member : collectivity.getMembers()) {
            if (oldEnoughMember >= 5) return;
            if (memberRepository.isLongTimeMember(member)) {
                oldEnoughMember++;
            }
        }
        throw new BadRequestException("Not enough old members");
    }

    public void validate(List<CreateCollectivityRequest> collectivities) throws BadRequestException {
        if (collectivities == null || collectivities.isEmpty()) {
            throw new BadRequestException("collectivities is null");
        }
        for (CreateCollectivityRequest collectivity : collectivities) {
            this.validate(collectivity);
        }
    }

    public void validate(String id, CollectivityInformation info) throws BadRequestException {
        if (info == null) {
            throw new BadRequestException("Information is null");
        }

        if (info.getName() == null || info.getName().trim().isEmpty()) {
            throw new BadRequestException("Name is required");
        }
        if (info.getNumber() == null || info.getNumber() <= 0) {
            throw new BadRequestException("Number is required");
        }

        // 2. Validation de l'unicité (Règle du YAML : 400 if name or number already used)
        if (collectivityRepository.existsByName(info.getName(), id)) {
            throw new BadRequestException("Name '" + info.getName() + "' is already used by another collectivity");
        }

        if (collectivityRepository.existsByNumber(info.getNumber(), id)) {
            throw new BadRequestException("Number '" + info.getNumber() + "' is already used by another collectivity");
        }
    }

    public void validateTransactionParameters(String id, LocalDate from, LocalDate to) throws BadRequestException {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("id is required");
        }
        if (from == null || to == null) {
            throw new BadRequestException("from and to cannot be null");
        }
        if (to.isBefore(from)) {
            throw new BadRequestException("to is before from");
        }
    }
    public void validateFinancialAccountParameters(String id, LocalDate at) throws BadRequestException {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("id is required");
        }

        if (at != null && at.isAfter(LocalDate.now())) {
            throw new BadRequestException("Parameter 'at' cannot be in the future");
        }
    }
}

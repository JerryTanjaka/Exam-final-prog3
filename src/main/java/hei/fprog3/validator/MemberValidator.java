package hei.fprog3.validator;

import hei.fprog3.dto.member.CreateMemberRequest;
import hei.fprog3.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberValidator {
    public void validate(CreateMemberRequest member) throws BadRequestException {
        if  (member == null) {
            throw new BadRequestException("member is null");
        }
        List<String> errors = new ArrayList<>();
        if (member.getFirstName() == null || member.getFirstName().isEmpty()) {
            errors.add("First name");
        }
        if (member.getLastName() == null || member.getLastName().isEmpty()) {
            errors.add("Last name");
        }
        if (member.getPhone() == null || member.getPhone().isEmpty()) {
            errors.add("Phone number");
        }
        if (member.getGender() == null) {
            errors.add("Gender");
        }
        if (member.getEmail() == null || member.getEmail().isEmpty()) {
            errors.add("Email");
        }
        if (member.getBirthDate() == null) {
            errors.add("Birth date");
        }
        if (member.getAddress() == null || member.getAddress().isEmpty()) {
            errors.add("Address");
        }
        if  (!errors.isEmpty()) {
            throw new BadRequestException(String.join(", ", errors) + (errors.size() == 1 ? " is" : " are") + " required");
        }
        if (!member.isRegistrationFeePaid()) {
            errors.add("Registration fee");
        }
        if (!member.isMembershipDuesPaid()) {
            errors.add("Membership dues");
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join(", ", errors) + (errors.size() == 1 ? " is" : " are") + " unpaid");
        }
    }

    public void validate(List<CreateMemberRequest> members) throws BadRequestException {
        for (CreateMemberRequest member : members) {
            validate(member);
        }
    }
}

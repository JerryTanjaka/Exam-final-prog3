package hei.fprog3.dto.member;

import hei.fprog3.model.Member;
import hei.fprog3.model.enums.PositionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateMemberRequest extends Member {
    private String collectivityIdentifier;
    private PositionType occupation;
    private List<String> referees; // IDs of the referee
    private boolean registrationFeePaid;
    private boolean membershipDuesPaid;

    public CreateMemberRequest() {}
}

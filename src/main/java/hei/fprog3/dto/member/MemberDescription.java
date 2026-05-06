package hei.fprog3.dto.member;

import hei.fprog3.model.enums.PositionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDescription {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private PositionType occupation;

    public MemberDescription fromMemberResponse(MemberResponse member) {
        this.id = member.getId();
        this.firstName = member.getFirstName();
        this.lastName = member.getLastName();
        this.email = member.getEmail();
        this.occupation = member.getOccupation();
        return this;
    }
}

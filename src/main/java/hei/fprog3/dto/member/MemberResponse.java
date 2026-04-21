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
public class MemberResponse extends Member {
    private PositionType occupation;
    private List<MemberResponse> referees;

    public MemberResponse() {}
}

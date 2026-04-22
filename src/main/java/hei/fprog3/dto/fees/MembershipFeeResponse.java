package hei.fprog3.dto.fees;


import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipFeeResponse extends CreateMembershipFee {
    private String id;
    private String status;
}
package hei.fprog3.dto.fees;


import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMembershipFee {
    private String label;
    private Double amount;
    private String frequency;
    private LocalDate eligibleFrom;
}
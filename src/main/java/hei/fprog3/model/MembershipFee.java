package hei.fprog3.model;

import hei.fprog3.model.enums.Frequency;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MembershipFee {
    private String id;
    private String collectivityId;
    private String label;
    private Double amount;
    private Frequency frequency;
    private LocalDate eligibleFrom;
    private String status;
}
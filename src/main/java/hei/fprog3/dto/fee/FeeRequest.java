package hei.fprog3.dto.fee;

import hei.fprog3.model.enums.FeeFrequencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeRequest {
    private LocalDate eligibleFrom;
    private FeeFrequencyType frequency;
    private double amount;
    private String label;
}

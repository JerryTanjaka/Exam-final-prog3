package hei.fprog3.dto.activity;

import hei.fprog3.model.enums.ActivityType;
import hei.fprog3.model.enums.PositionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBase {
    private String label;
    private ActivityType activityType;
    private List<PositionType> memberOccupationConcerned;
    private LocalDate executiveDate;
    private ActivityRecurrenceRule recurrenceRule;
}

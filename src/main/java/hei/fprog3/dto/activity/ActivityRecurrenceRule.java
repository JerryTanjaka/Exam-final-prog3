package hei.fprog3.dto.activity;

import hei.fprog3.model.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRecurrenceRule {
    private int weekOrdinal;
    private DayOfWeek dayOfWeek;
}

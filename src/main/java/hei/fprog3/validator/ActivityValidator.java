package hei.fprog3.validator;

import hei.fprog3.dto.activity.ActivityCreate;
import hei.fprog3.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ActivityValidator {
    public void validate(ActivityCreate activity) throws BadRequestException {
        if (activity == null) {
            throw new BadRequestException("Activity is missing");
        }
        List<String> errors = new ArrayList<>();
        if (activity.getLabel() == null ||  activity.getLabel().isEmpty()) {
            errors.add("Label is not defined");
        }
        if (activity.getActivityType() == null) {
            errors.add("Activity type not defined");
        }
        if ((activity.getMemberOccupationConcerned() == null)
                || activity.getMemberOccupationConcerned().isEmpty()) {
            errors.add("No member occupation concerned");
        }
        if (activity.getRecurrenceRule() != null && activity.getExecutiveDate() != null) {
            errors.add("Recurrence rule and executive date cannot be set together");
        } else if (activity.getRecurrenceRule() != null) {
            if (activity.getRecurrenceRule().getDayOfWeek() == null) {
                errors.add("Day of week not defined");
            }
            if (activity.getRecurrenceRule().getWeekOrdinal() < 1
                    || activity.getRecurrenceRule().getWeekOrdinal() > 5) {
                errors.add("Week ordinal must be between 1 and 5");
            }
        } else if (activity.getExecutiveDate() != null) {
        } else {
            errors.add("Either recurrence rule or executive date must be defined");
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join(", ", errors));
        }
    }

    public void validate(List<ActivityCreate> activities) throws BadRequestException {
        for (ActivityCreate activity : activities) {
            validate(activity);
        }
    }
}

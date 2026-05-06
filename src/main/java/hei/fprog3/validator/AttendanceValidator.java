package hei.fprog3.validator;

import hei.fprog3.dto.attendance.AttendanceRequest;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.model.enums.AttendanceStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AttendanceValidator {
    public void validate(AttendanceRequest attendance) throws BadRequestException {
        List<String> errors = new ArrayList<>();
        if (attendance.getMemberIdentifier() == null ||  attendance.getMemberIdentifier().isEmpty()) {
            errors.add("Member identifier is required");
        }
        if (attendance.getAttendanceStatus() == null) {
            errors.add("Attendance status is null");
        } else if (attendance.getAttendanceStatus() == AttendanceStatus.UNDEFINED) {
            errors.add("Attendance status cannot be set to UNDEFINED");
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(String.join(", ", errors));
        }
    }

    public void validate(List<AttendanceRequest> attendances) throws BadRequestException {
        for (AttendanceRequest attendance : attendances) {
            validate(attendance);
        }
    }
}

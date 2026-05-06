package hei.fprog3.dto.attendance;

import hei.fprog3.model.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRequest {
    private String memberIdentifier;
    private AttendanceStatus attendanceStatus;
}

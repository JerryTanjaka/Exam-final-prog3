package hei.fprog3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommunityChange {

    private int id;
    private int memberId;
    private int oldCommunityId;
    private int newCommunityId;
    private String reason;
    private LocalDate changeDate;
    private LocalDateTime createdAt;

    public CommunityChange() {}

}

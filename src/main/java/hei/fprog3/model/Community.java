package hei.fprog3.model;

import hei.fprog3.model.enums.CommunityStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class Community {

    private int id;
    private String number;
    private String name;
    private String city;
    private String agriculturalSpecialty;
    private LocalDate creationDate;
    private CommunityStatus status;
    private String authorizationComment;
    private LocalDate authorizationDate;
    private BigDecimal mandatoryAnnualContribution;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Community() {}


    @Override
    public String toString() {
        return "Community{id=" + id + ", number='" + number + "', name='" + name + "', status=" + status + "}";
    }
}

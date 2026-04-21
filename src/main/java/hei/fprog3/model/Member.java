package hei.fprog3.model;

import hei.fprog3.model.enums.GenderType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Représente un membre de la fédération.
 * Table : members
 */
public class Member {

    private int id;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private GenderType gender;
    private String address;
    private String occupation;
    private String phone;
    private String email;
    private LocalDate membershipDate;
    private int communityId;
    private boolean active;
    private LocalDate resignationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Member() {}

    @Override
    public String toString() {
        return "Member{id=" + id + ", name='" + lastName + " " + firstName + "', active=" + active + "}";
    }
}

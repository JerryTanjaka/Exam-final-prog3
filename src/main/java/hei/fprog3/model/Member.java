package hei.fprog3.model;

import hei.fprog3.model.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Member {

    private String id;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private GenderType gender;
    private String address;
    private String profession;
    private String phone;
    private String email;

    public Member() {}

    @Override
    public String toString() {
        return "Member{id=" + id + ", name='" + lastName + " " + firstName + "}";
    }
}

package hei.fprog3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class Collectivity {

    private String id;
    private String number;
    private String name;
    private String location;
    private String specialty;
    private LocalDate creationDate;

    public Collectivity() {}

    @Override
    public String toString() {
        return "Community{id=" + id + ", number='" + number + "', name='" + name + "}";
    }
}

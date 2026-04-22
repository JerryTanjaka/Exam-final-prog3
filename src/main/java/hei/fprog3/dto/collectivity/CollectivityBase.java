package hei.fprog3.dto.collectivity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class CollectivityBase {

    private String id;
    private String location;
    private String specialty;
    private LocalDate creationDate;

    public CollectivityBase() {}

}

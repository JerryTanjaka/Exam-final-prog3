package hei.fprog3.dto.collectivity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CollectivityStructureRequest {
    private String president;
    private String vicePresident;
    private String treasurer;
    private String secretary;
}

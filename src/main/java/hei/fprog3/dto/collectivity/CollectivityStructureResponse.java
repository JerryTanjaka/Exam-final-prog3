package hei.fprog3.dto.collectivity;

import hei.fprog3.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CollectivityStructureResponse {
    private Member president;
    private Member vicePresident;
    private Member treasurer;
    private Member secretary;

    public CollectivityStructureResponse() {}
}

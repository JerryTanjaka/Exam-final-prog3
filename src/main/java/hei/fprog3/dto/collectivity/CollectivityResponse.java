package hei.fprog3.dto.collectivity;

import hei.fprog3.model.Collectivity;
import hei.fprog3.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CollectivityResponse extends Collectivity {
    private CollectivityStructureResponse structure;
    private List<Member> members;

    public CollectivityResponse() {}
}

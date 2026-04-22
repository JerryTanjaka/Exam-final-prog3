package hei.fprog3.dto.collectivity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateCollectivityRequest extends CollectivityBase {
    private List<String> members;
    private boolean federationApproval;
    private CollectivityStructureRequest structure;
}

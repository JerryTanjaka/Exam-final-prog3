package hei.fprog3.dto.collectivity;

import hei.fprog3.model.Collectivity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateCollectivityRequest extends Collectivity {
    private List<String> members;
    private boolean federationApproval;
    private CollectivityStructureRequest structure;
}

package hei.fprog3.dto.collectivity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hei.fprog3.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CollectivityResponse extends CollectivityBase {
    private CollectivityStructureResponse structure;
    private List<Member> members;
    @JsonIgnore
    private CollectivityInformation identity;
    public CollectivityResponse() {}

    public String getName() {
        if (identity != null) {
            return this.getIdentity().getName();
        }
        return null;
    }

    public Integer getNumber() {
        if (identity != null) {
            return this.getIdentity().getNumber();
        }
        return null;
    }
}

package hei.fprog3.model;

import hei.fprog3.dto.fee.FeeRequest;
import hei.fprog3.model.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fee extends FeeRequest {
    private String id;
    private StatusType status;
}

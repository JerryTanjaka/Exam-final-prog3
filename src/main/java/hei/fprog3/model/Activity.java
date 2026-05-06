package hei.fprog3.model;

import hei.fprog3.dto.activity.ActivityBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Activity extends ActivityBase {
    private String id;
}

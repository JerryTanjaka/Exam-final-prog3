package hei.fprog3.dto.statistic;

import hei.fprog3.dto.member.MemberDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatistic {
    private MemberDescription memberDescription;
    private double earnedAmount;
    private double unpaidAmount;
}

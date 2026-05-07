package hei.fprog3.dto.statistic;

import hei.fprog3.dto.collectivity.CollectivityInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectivityOverallStatistics {
    private CollectivityInformation collectivityInformation;
    private int newMembersNumber;
    private double overallMemberCurrentDuePercentage;
    private double overallMemberAssiduityPercentage;
}
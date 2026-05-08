package hei.fprog3.api.model;

public class CollectivityOverallStatistics {

    public CollectivityInformation collectivityInformation;
    public Integer newMembersNumber;
    public Double overallMemberCurrentDuePercentage;
    public Double overallMemberAssiduityPercentage;

    @Override
    public String toString() {
        return "CollectivityOverallStatistics{" +
                "collectivityInformation=" + collectivityInformation +
                ", newMembersNumber=" + newMembersNumber +
                ", overallMemberCurrentDuePercentage=" + overallMemberCurrentDuePercentage +
                ", overallMemberAssiduityPercentage=" + overallMemberAssiduityPercentage +
                '}';
    }
}

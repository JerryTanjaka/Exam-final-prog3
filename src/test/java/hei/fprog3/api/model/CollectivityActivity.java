package hei.fprog3.api.model;

public class CollectivityActivity extends CreateCollectivityActivity {

    public String id;

    @Override
    public String toString() {
        return "CollectivityActivity{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", activityType=" + activityType +
                ", memberOccupationConcerned=" + memberOccupationConcerned +
                ", recurrenceRule=" + recurrenceRule +
                ", executiveDate=" + executiveDate +
                '}';
    }
}

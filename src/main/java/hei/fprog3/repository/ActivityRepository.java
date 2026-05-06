package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.activity.ActivityRecurrenceRule;
import hei.fprog3.model.Activity;
import hei.fprog3.model.enums.ActivityType;
import hei.fprog3.model.enums.DayOfWeek;
import hei.fprog3.model.enums.PositionType;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActivityRepository {
    private DataSourceConfig dataSource;
    public ActivityRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    public List<Activity> getAllActivities(String id) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement activityPs = connection.prepareStatement(
                    """
                    SELECT a.id, a.label, a.type, a.executive_date, a.week_ordinal, a.day_of_week
                    FROM activities AS a WHERE collectivity_id = ?
                    """);
            activityPs.setString(1, id);
            PreparedStatement membersPs = connection.prepareStatement(
                    """
                    SELECT DISTINCT required_member 
                    FROM activity_required_members AS arm
                    WHERE arm.activity_id = ?
                    """
            );
            ResultSet activityRs = activityPs.executeQuery();
            List<Activity> activities = new ArrayList<>();
            while (activityRs.next()) {
                membersPs.setString(1, activityRs.getString("id"));
                ResultSet memberRs = membersPs.executeQuery();
                List<PositionType> requiredMembers = new ArrayList<>();
                while (memberRs.next()) {
                    requiredMembers.add(PositionType.valueOf(memberRs.getString("required_member")));
                }
                Activity activity = new Activity();
                activity.setId(activityRs.getString("id"));
                activity.setLabel(activityRs.getString("label"));
                activity.setActivityType(ActivityType.valueOf(activityRs.getString("type")));
                activity.setExecutiveDate(activityRs.getDate("executive_date").toLocalDate());
                activity.setMemberOccupationConcerned(requiredMembers);
                activity.setRecurrenceRule(new ActivityRecurrenceRule(
                        activityRs.getInt("week_ordinal"),
                        DayOfWeek.valueOf(activityRs.getString("day_of_week")))
                );
                activities.add(activity);
            }
            return activities;
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}

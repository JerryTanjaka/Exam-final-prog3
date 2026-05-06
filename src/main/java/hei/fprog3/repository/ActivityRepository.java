package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.activity.ActivityCreate;
import hei.fprog3.dto.activity.ActivityRecurrenceRule;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Activity;
import hei.fprog3.model.enums.ActivityType;
import hei.fprog3.model.enums.DayOfWeek;
import hei.fprog3.model.enums.PositionType;
import org.springframework.stereotype.Repository;

import java.sql.*;
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

    public List<Activity> createActivityAndReturn(String id, List<ActivityCreate> newActivities) {
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement activityPs = connection.prepareStatement(
                """
                INSERT INTO activities (collectivity_id, label, type, executive_date, week_ordinal, day_of_week)
                VALUES (?, ?, ?::activity_type, ?, ?, ?::day_of_week_type)
                """, Statement.RETURN_GENERATED_KEYS);

            PreparedStatement membersPs = connection.prepareStatement(
                """
                INSERT INTO activity_required_members (activity_id, required_member)
                VALUES (?, ?::position_type)
                """);

            for (ActivityCreate activity : newActivities) {
                activityPs.setString(1, id);
                activityPs.setString(2, activity.getLabel());
                activityPs.setString(3, activity.getActivityType().name());

                if (activity.getExecutiveDate() != null) {
                    activityPs.setDate(4, Date.valueOf(activity.getExecutiveDate()));
                } else {
                    activityPs.setNull(4, Types.DATE);
                }

                if (activity.getRecurrenceRule() != null) {
                    activityPs.setInt(5, activity.getRecurrenceRule().getWeekOrdinal());
                    activityPs.setString(6, activity.getRecurrenceRule().getDayOfWeek().name());
                } else {
                    activityPs.setNull(5, Types.INTEGER);
                    activityPs.setNull(6, Types.VARCHAR);
                }

                activityPs.addBatch();
            }
            activityPs.executeBatch();

            ResultSet activityRs = activityPs.getGeneratedKeys();
            List<String> newActivitiesIds = new ArrayList<>();
            while (activityRs.next()) {
                newActivitiesIds.add(activityRs.getString("id"));
            }

            for (String activityId : newActivitiesIds) {
                List<PositionType> requiredMemberPositions = newActivities.get(newActivitiesIds.indexOf(activityId)).getMemberOccupationConcerned();
                for (PositionType position : requiredMemberPositions) {
                    membersPs.setString(1, activityId);
                    membersPs.setString(2, position.name());
                    membersPs.addBatch();
                };
            }
            membersPs.executeBatch();

            List<Activity> activities = new ArrayList<>();
            for (String newActivityId : newActivitiesIds) {
                activities.add(getActivityById(connection, newActivityId));
            }
            connection.commit();
            return activities;
        } catch (SQLException | RuntimeException e) {
            dataSource.rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    private Activity getActivityById(Connection connection, String id) throws SQLException {
        PreparedStatement activityPs = connection.prepareStatement(
                """
                SELECT a.id, a.label, a.type, a.executive_date, a.week_ordinal, a.day_of_week
                FROM activities AS a WHERE a.id = ?
                """);
        activityPs.setString(1, id);
        PreparedStatement membersPs = connection.prepareStatement(
                """
                SELECT DISTINCT required_member
                FROM activity_required_members AS arm
                WHERE arm.activity_id = ?
                """
        );
        membersPs.setString(1, id);
        ResultSet activityRs = activityPs.executeQuery();
        if (activityRs.next()) {
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
            activity.setMemberOccupationConcerned(requiredMembers);

            if (activityRs.getObject("executive_date") != null) {
                activity.setExecutiveDate(activityRs.getDate("executive_date").toLocalDate());
            } else {
                activity.setExecutiveDate(null);
            }

            if (activityRs.getObject("week_ordinal") != null && activityRs.getObject("day_of_week") != null) {
                activity.setRecurrenceRule(new ActivityRecurrenceRule(
                        activityRs.getInt("week_ordinal"),
                        DayOfWeek.valueOf(activityRs.getString("day_of_week"))));
            } else {
                activity.setRecurrenceRule(null);
            }

            return activity;
        }
        return null;
    }

    public void exists(String id) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            if (getActivityById(connection, id) == null) {
                throw new NotFoundException(id);
            };
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }
}

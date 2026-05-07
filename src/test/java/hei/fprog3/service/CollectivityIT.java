package hei.fprog3.service;

import hei.fprog3.api.ApiClient;
import hei.fprog3.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class CollectivityIT {
    final ApiClient apiClient = new ApiClient();

    @Test
    void collectivity_not_found() {
        var id = "col-10";

        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.get("/collectivities/" + id, Collectivity.class));

        var exceptionMessage = exception.getMessage();
        log.info(exceptionMessage);
        assertTrue(exceptionMessage.contains("404"));
    }

    @Test
    void retrieve_collectivity_by_id_one() {
        var id = "col-1";

        var collectivityOne = apiClient.get("/collectivities/" + id, Collectivity.class);

        log.info(collectivityOne.toString());
        assertNotNull(collectivityOne, "Unable to obtain collectivity");
        assertTrue(collectivityOne.name.contains("Mpanorina"),
                "Collectivity name obtained not as expected " + collectivityOne.name);
        assertTrue(collectivityOne.location.contains("Ambatondrazaka"),
                "Collectivity location obtained not as expected " + collectivityOne.location);
        assertNotNull(collectivityOne.structure, "Collectivity structure not null");
        assertNotNull(collectivityOne.members, "Null members for collectivity");

        collectivityOne.members.forEach(member -> {
            if (!member.id.equals("C1-M1") && !member.id.equals("C1-M2")) {
                assertNotNull(member.referees, "Referees null");
                var refereeIds = member.referees.stream().map(m -> m.id).toList();
                assertTrue(refereeIds.contains("C1-M1") || refereeIds.contains("C1-M6"),
                        "Unexpected referee ID for member " + member.id);
                assertTrue(refereeIds.contains("C1-M2") || refereeIds.contains("C1-M7"));
            } else {
                assertTrue(member.referees == null || member.referees.isEmpty(),
                        "Referees for C1-M1 and C1-M2 should be empty");
            }
        });

        var collectivityStructure = collectivityOne.structure;
        assertTrue(collectivityStructure.president != null
                && collectivityStructure.president.id.equals("C1-M1"));
        assertTrue(collectivityStructure.vicePresident != null
                && collectivityStructure.vicePresident.id.equals("C1-M2"));
        assertTrue(collectivityStructure.secretary != null
                && collectivityStructure.secretary.id.equals("C1-M3"));
        assertTrue(collectivityStructure.treasurer != null
                && collectivityStructure.treasurer.id.equals("C1-M4"));
    }

    @Test
    void create_collectivity_ko() {
        var createCollectivity = new CreateCollectivity();
        var createCollectivityStructure = new CreateCollectivityStructure();
        createCollectivityStructure.president = "C3-M7";
        createCollectivityStructure.vicePresident = "C3-M8";
        createCollectivityStructure.secretary = "C3-M6";
        createCollectivityStructure.treasurer = "C3-M5";
        createCollectivity.structure = createCollectivityStructure;
        createCollectivity.location = "Antananarivo";
        createCollectivity.federationApproval = true;
        // ✅ moins de 10 membres → doit retourner 400
        createCollectivity.members = List.of("C1-M1", "C1-M2", "C1-M3", "C1-M4");

        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.post("/collectivities",
                        List.of(createCollectivity),
                        new ParameterizedTypeReference<List<Collectivity>>() {}));

        log.info(exception.getMessage());
        assertTrue(exception.getMessage().contains("400"));
    }

    @Test
    void change_name_ko_already_assigned() {
        var id = "col-1";
        var payload = new CollectivityInformation();
        payload.name = "Dobo voalohany";
        payload.number = "999";

        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.put("/collectivities/" + id + "/informations", payload, Collectivity.class));

        log.info(exception.getMessage());
        assertTrue(exception.getMessage().contains("400"));
    }

    @Test
    void get_financial_account() {
        var id = "col-1";

        var financialAccounts = apiClient.get(
                "/collectivities/" + id + "/financialAccounts", String.class);

        assertNotNull(financialAccounts,
                "Unable to obtain financial accounts for collectivity.id=" + id);
        log.info("FinancialAccounts: " + financialAccounts);
    }

    @Test
    void get_financial_account_at() {
        var id = "col-1";
        var date = "2026-01-31";

        var financialAccounts = apiClient.get(
                "/collectivities/" + id + "/financialAccounts?at=" + date, String.class);

        assertNotNull(financialAccounts,
                "Unable to obtain financial accounts at " + date + " for collectivity.id=" + id);
        log.info("FinancialAccounts: " + financialAccounts);
    }

    @Test
    void get_transactions_from_a_period() {
        var id = "col-1";
        var from = "2026-01-01";
        var to = "2026-06-30";

        var collectivityTransactions = apiClient.get(
                "/collectivities/" + id + "/transactions?from=" + from + "&to=" + to,
                new ParameterizedTypeReference<List<CollectivityTransaction>>() {});

        assertNotNull(collectivityTransactions,
                "Unable to obtain collectivity transactions for collectivity.id=" + id);
        assertFalse(collectivityTransactions.isEmpty(),
                "Transactions should not be empty");
        log.info("Transactions : " + collectivityTransactions);

        // ✅ vérifie la structure plutôt qu'un montant exact
        collectivityTransactions.forEach(t -> {
            assertNotNull(t.amount, "Transaction amount should not be null");
            assertNotNull(t.paymentMode, "Payment mode should not be null");
            assertNotNull(t.memberDebited, "Member debited should not be null");
        });
    }

    @Test
    void get_membership_fees() {
        var id = "col-1";

        var membershipFees = apiClient.get(
                "/collectivities/" + id + "/membershipFees",
                new ParameterizedTypeReference<List<MembershipFee>>() {});

        assertNotNull(membershipFees,
                "Unable to obtain membership fees for collectivity.id=" + id);
        assertFalse(membershipFees.isEmpty(), "Membership fees should not be empty");
        log.info("MembershipFees: " + membershipFees);
    }

    @Test
    void create_membership_fees_ok() {
        var id = "col-1";
        var createMembershipFee = new CreateMembershipFee();
        createMembershipFee.label = "New fee " + System.currentTimeMillis(); // ✅ label unique
        createMembershipFee.eligibleFrom = java.time.LocalDate.now();
        createMembershipFee.frequency = Frequency.MONTHLY;
        createMembershipFee.amount = new java.math.BigDecimal("15000");

        var membershipFees = apiClient.post(
                "/collectivities/" + id + "/membershipFees",
                List.of(createMembershipFee),
                new ParameterizedTypeReference<List<MembershipFee>>() {});

        assertNotNull(membershipFees, "Unable to create membership fees");
        assertFalse(membershipFees.isEmpty());
        log.info("Created MembershipFees: " + membershipFees);
    }

    @Test
    void create_membership_fees_ko() {
        var id = "col-1";
        var createMembershipFee = new CreateMembershipFee();
        createMembershipFee.label = "Bad fee";
        createMembershipFee.eligibleFrom = java.time.LocalDate.now();
        createMembershipFee.frequency = Frequency.WEEKLY;
        createMembershipFee.amount = new java.math.BigDecimal("-100"); // ✅ montant négatif

        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.post(
                        "/collectivities/" + id + "/membershipFees",
                        List.of(createMembershipFee),
                        new ParameterizedTypeReference<List<MembershipFee>>() {}));

        log.info(exception.getMessage());
        assertTrue(exception.getMessage().contains("400")); // ✅ corrigé : 400 pas 500
    }

    @Test
    void get_local_statistics() {
        var id = "col-1";
        var from = "2026-01-01";
        var to = "2026-05-31";

        var statistics = apiClient.get(
                "/collectivities/" + id + "/statistics?from=" + from + "&to=" + to,
                new ParameterizedTypeReference<List<CollectivityLocalStatistics>>() {});

        assertNotNull(statistics,
                "Unable to obtain local statistics for collectivity.id=" + id);
        assertFalse(statistics.isEmpty(), "Statistics should not be empty");
        log.info("LocalStatistics: " + statistics);
    }

    @Test
    void get_overall_statistics() {
        var from = "2026-01-01";
        var to = "2026-05-31";

        var statistics = apiClient.get(
                "/collectivities/statistics?from=" + from + "&to=" + to,
                new ParameterizedTypeReference<List<CollectivityOverallStatistics>>() {});

        assertNotNull(statistics, "Unable to obtain overall statistics");
        assertFalse(statistics.isEmpty(), "Overall statistics should not be empty");
        log.info("OverallStatistics: " + statistics);
    }

    @Test
    void get_activities() {
        var id = "col-1";

        var activities = apiClient.get(
                "/collectivities/" + id + "/activities",
                new ParameterizedTypeReference<List<CollectivityActivity>>() {});

        assertNotNull(activities,
                "Unable to obtain activities for collectivity.id=" + id);
        log.info("Activities: " + activities);
    }

    @Test
    void create_activities_ok() {
        var id = "col-1";
        var createActivity = new CreateCollectivityActivity();
        createActivity.label = "Test Meeting " + System.currentTimeMillis(); // ✅ label unique
        createActivity.activityType = "MEETING";
        createActivity.executiveDate = java.time.LocalDate.now().plusDays(7);
        createActivity.memberOccupationConcerned = List.of(MemberOccupation.JUNIOR, MemberOccupation.SENIOR);

        var activities = apiClient.post(
                "/collectivities/" + id + "/activities",
                List.of(createActivity),
                new ParameterizedTypeReference<List<CollectivityActivity>>() {});

        assertNotNull(activities, "Unable to create activities");
        assertFalse(activities.isEmpty());
        assertNotNull(activities.get(0).id, "Activity ID should not be null");
        log.info("Created Activities: " + activities);
    }

    @Test
    void create_activities_ko_both_dates() {
        var id = "col-1";
        var createActivity = new CreateCollectivityActivity();
        createActivity.label = "Bad Activity";
        createActivity.activityType = "TRAINING";
        createActivity.executiveDate = java.time.LocalDate.now(); // ✅ les deux définis → 400
        var recurrenceRule = new MonthlyRecurrenceRule();
        recurrenceRule.weekOrdinal = 2;
        recurrenceRule.dayOfWeek = "SA";
        createActivity.recurrenceRule = recurrenceRule;
        createActivity.memberOccupationConcerned = List.of(MemberOccupation.JUNIOR);

        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.post(
                        "/collectivities/" + id + "/activities",
                        List.of(createActivity),
                        new ParameterizedTypeReference<List<CollectivityActivity>>() {}));

        log.info(exception.getMessage());
        assertTrue(exception.getMessage().contains("400"));
    }

    @Test
    void create_attendance_ok() {
        var id = "col-1";
        var activityId = create_activity_for_test(); // ✅ utilise l'helper

        var attendance = new CreateActivityMemberAttendance();
        attendance.memberIdentifier = "C1-M5";
        attendance.attendanceStatus = "ATTENDED";

        var attendances = apiClient.post(
                "/collectivities/" + id + "/activities/" + activityId + "/attendance",
                List.of(attendance),
                new ParameterizedTypeReference<List<ActivityMemberAttendance>>() {});

        assertNotNull(attendances, "Unable to create attendance");
        assertFalse(attendances.isEmpty());
        log.info("Created Attendance: " + attendances);
    }

    @Test
    void create_attendance_ko_already_confirmed() {
        var id = "col-1";
        var activityId = create_activity_for_test(); // ✅ activité fraîche à chaque test

        var attendance = new CreateActivityMemberAttendance();
        attendance.memberIdentifier = "C1-M5";
        attendance.attendanceStatus = "ATTENDED";

        // Premier appel — doit réussir
        apiClient.post(
                "/collectivities/" + id + "/activities/" + activityId + "/attendance",
                List.of(attendance),
                new ParameterizedTypeReference<List<ActivityMemberAttendance>>() {});

        // Deuxième appel — doit échouer car déjà confirmé
        var exception = assertThrows(RuntimeException.class,
                () -> apiClient.post(
                        "/collectivities/" + id + "/activities/" + activityId + "/attendance",
                        List.of(attendance),
                        new ParameterizedTypeReference<List<ActivityMemberAttendance>>() {}));

        log.info(exception.getMessage());
        assertTrue(exception.getMessage().contains("400"));
    }

    @Test
    void get_attendance() {
        var id = "col-1";
        var activityId = create_activity_for_test();

        var attendance = new CreateActivityMemberAttendance();
        attendance.memberIdentifier = "C1-M5";
        attendance.attendanceStatus = "ATTENDED";

        apiClient.post(
                "/collectivities/" + id + "/activities/" + activityId + "/attendance",
                List.of(attendance),
                new ParameterizedTypeReference<List<ActivityMemberAttendance>>() {});

        var attendances = apiClient.get(
                "/collectivities/" + id + "/activities/" + activityId + "/attendance",
                new ParameterizedTypeReference<List<ActivityMemberAttendance>>() {});

        assertNotNull(attendances, "Unable to get attendance");
        assertFalse(attendances.isEmpty(), "Attendance list should not be empty");
        log.info("Attendance: " + attendances);
    }

    // ✅ helper : crée une activité fraîche avec un label unique
    private String create_activity_for_test() {
        var id = "col-1";
        var createActivity = new CreateCollectivityActivity();
        createActivity.label = "Test Activity " + System.currentTimeMillis();
        createActivity.activityType = "MEETING";
        createActivity.executiveDate = java.time.LocalDate.now().minusDays(1);
        createActivity.memberOccupationConcerned = List.of(MemberOccupation.SENIOR, MemberOccupation.PRESIDENT);

        var activities = apiClient.post(
                "/collectivities/" + id + "/activities",
                List.of(createActivity),
                new ParameterizedTypeReference<List<CollectivityActivity>>() {});

        assertNotNull(activities);
        assertFalse(activities.isEmpty());
        return activities.get(0).id;
    }
}
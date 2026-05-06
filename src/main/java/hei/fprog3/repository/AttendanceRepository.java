package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.attendance.AttendanceRequest;
import hei.fprog3.dto.attendance.AttendanceResponse;
import hei.fprog3.dto.member.MemberDescription;
import hei.fprog3.exception.BadRequestException;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.enums.AttendanceStatus;
import hei.fprog3.model.enums.PositionType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AttendanceRepository {
    private DataSourceConfig dataSource;
    private final MemberRepository memberRepository;
    public AttendanceRepository(DataSourceConfig dataSource, MemberRepository memberRepository) {
        this.dataSource = dataSource;
        this.memberRepository = memberRepository;
    }

    public List<AttendanceResponse> createAndReturn(String activityId,
                                                    List<AttendanceRequest> attendanceRequests) throws BadRequestException, NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);

            Map<String, AttendanceStatus> attendanceStatusMap = attendanceRequests.stream().collect(Collectors.toMap(
                    AttendanceRequest::getMemberIdentifier,
                    AttendanceRequest::getAttendanceStatus,
                    (existing, replacement) ->  replacement));

            PreparedStatement attendancePs = connection.prepareStatement(
                    """
                    SELECT aa.id, aa.status FROM activity_attendances AS aa
                    WHERE activity_id = ? AND member_id = ?
                    """
            );

            PreparedStatement insertPs = connection.prepareStatement(
                    """
                    INSERT INTO activity_attendances (activity_id, member_id, status)
                    VALUES (?, ?, ?::attendance_status)
                    ON CONFLICT (activity_id, member_id)
                    DO UPDATE SET status = EXCLUDED.status
                    RETURNING id
                    """, Statement.RETURN_GENERATED_KEYS);

            for (String memberId : attendanceStatusMap.keySet()) {
                memberRepository.findById(memberId);
                attendancePs.setString(1, activityId);
                attendancePs.setString(2, memberId);
                ResultSet attendanceRs = attendancePs.executeQuery();
                if (attendanceRs.next()) {
                    if (!attendanceRs.getString("status").equals("UNDEFINED")) {
                        throw new BadRequestException("Member %s status has already been confirmed".formatted(memberId));
                    }
                }
            }

            for (String memberId : attendanceStatusMap.keySet()) {
                insertPs.setString(1, activityId);
                insertPs.setString(2, memberId);
                insertPs.setString(3, attendanceStatusMap.get(memberId).name());
                insertPs.addBatch();
            }
            insertPs.executeBatch();

            ResultSet attendanceRs = insertPs.getGeneratedKeys();
            List<String> newAttendanceIds = new ArrayList<>();
            while (attendanceRs.next()) {
                newAttendanceIds.add(attendanceRs.getString("id"));
            }

            List<AttendanceResponse> attendanceResponses = new ArrayList<>();
            for (String newAttendanceId : newAttendanceIds) {
                attendanceResponses.add(getAttendanceById(connection, newAttendanceId));
            }

            connection.commit();
            return attendanceResponses;
        } catch (SQLException | RuntimeException e) {
            dataSource.rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    private AttendanceResponse getAttendanceById(Connection connection, String attendanceId) throws NotFoundException, SQLException {
        PreparedStatement ps = connection.prepareStatement(
                """
                SELECT aa.id, aa.status, aa.member_id, aa.activity_id, collectivity_id FROM activity_attendances AS aa
                JOIN activities ON aa.activity_id = activities.id
                WHERE aa.id = ?
                """
        );

        PreparedStatement memberPs = connection.prepareStatement(
                """
                SELECT m.id, m.first_name, m.last_name, m.email, ms.occupation FROM members AS m
                JOIN memberships AS ms ON m.id = ms.member_id
                WHERE ms.collectivity_id = ? AND ms.member_id = ?
                GROUP BY m.id, ms.occupation
                ORDER BY m.id, MAX(ms.start_date) DESC
                """);

        ps.setString(1, attendanceId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            AttendanceResponse attendanceResponse = new AttendanceResponse();
            attendanceResponse.setId(rs.getString("id"));
            attendanceResponse.setAttendanceStatus(AttendanceStatus.valueOf(rs.getString("status")));

            memberPs.setString(1, rs.getString("collectivity_id"));
            memberPs.setString(2, rs.getString("member_id"));
            ResultSet memberRs = memberPs.executeQuery();
            if (memberRs.next()) {
                attendanceResponse.setMemberDescription(
                        new MemberDescription(
                                memberRs.getString(1),
                                memberRs.getString(2),
                                memberRs.getString(3),
                                memberRs.getString(4),
                                PositionType.valueOf(memberRs.getString(5))
                        )
                );
            }
            return attendanceResponse;
        }
        throw new NotFoundException("No such attendance with id " + attendanceId);
    }
}

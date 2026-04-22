package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.member.CreateMemberRequest;
import hei.fprog3.dto.member.MemberResponse;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.enums.GenderType;
import hei.fprog3.model.enums.PositionType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Repository
public class MemberRepository {
    private DataSourceConfig dataSource;
    public MemberRepository(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    public List<MemberResponse> create(List<CreateMemberRequest> members) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            List<MemberResponse> memberResponses = new ArrayList<>();
            connection.setAutoCommit(false);
            for (CreateMemberRequest member : members) {
                member.setId(UUID.randomUUID().toString());
                PreparedStatement memberPs = connection.prepareStatement(
                        """
                        INSERT INTO members (last_name, first_name, birth_date, gender, address, profession, phone, email, id)
                        VALUES (?, ?, ?, ?::gender_type, ?, ?, ?, ?, ?::UUID)
                        """
                );
                memberPs.setString(1, member.getLastName());
                memberPs.setString(2, member.getFirstName());
                memberPs.setDate(3, Date.valueOf(member.getBirthDate()));
                memberPs.setString(4, member.getGender().name());
                memberPs.setString(5, member.getAddress());
                memberPs.setString(6, member.getProfession());
                memberPs.setString(7, member.getPhone());
                memberPs.setString(8, member.getEmail());
                memberPs.setObject(9, member.getId());
                memberPs.executeUpdate();

                if (member.getCollectivityIdentifier() != null
                        && !member.getCollectivityIdentifier().isEmpty()) {
                    PreparedStatement membershipPs = connection.prepareStatement(
                            """
                            INSERT INTO memberships (member_id, collectivity_id, occupation)
                            VALUES (?::UUID, ?::UUID, ?::position_type);
                            """
                    );
                    membershipPs.setString(1, member.getId());
                    membershipPs.setString(2, member.getCollectivityIdentifier());
                    membershipPs.setString(3, member.getOccupation().name());
                    membershipPs.executeUpdate();
                }

                if (member.getReferees() != null
                        && !member.getReferees().isEmpty()) {
                    for  (String refereeId : member.getReferees()) {
                        PreparedStatement refereePs = connection.prepareStatement(
                                """
                                INSERT INTO referals (member_id, referee_id)
                                VALUES (?::UUID, ?::UUID);
                                """
                        );
                        refereePs.setString(1, member.getId());
                        refereePs.setString(2, refereeId);
                        refereePs.executeUpdate();
                    }
                }
            }
            connection.commit();
            for (CreateMemberRequest member : members) {
                memberResponses.add(findById(member.getId()));
            }
            return memberResponses;
        } catch (SQLException e) {
            dataSource.rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public MemberResponse findById(String id) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement membersPs = connection.prepareStatement("""
                        SELECT id, first_name, last_name, birth_date, gender, address, profession, phone, email
                        FROM members WHERE id = ?::UUID
                        """);
            membersPs.setString(1, id);

            MemberResponse member = new MemberResponse();

            ResultSet membersRs = membersPs.executeQuery();
            if (membersRs.next()) {
                member.setId(membersRs.getString("id"));
                member.setFirstName(membersRs.getString("first_name"));
                member.setLastName(membersRs.getString("last_name"));
                member.setBirthDate(membersRs.getDate("birth_date").toLocalDate());
                member.setGender(GenderType.valueOf(membersRs.getString("gender")));
                member.setAddress(membersRs.getString("address"));
                member.setProfession(membersRs.getString("profession"));
                member.setPhone(membersRs.getString("phone"));
                member.setEmail(membersRs.getString("email"));
                member.setOccupation(getLatestOccupation(id));
            } else {
                throw new NotFoundException("Member with id %s not found".formatted(id));
            }

            member.setReferees(getMemberReferees(id));
            for (MemberResponse referee : member.getReferees()) {
                referee.setOccupation(this.getLatestOccupation(referee.getId()));
            }

            return member;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidReferee(String memberId, String collectivityId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("""
                        SELECT id
                        FROM memberships WHERE member_id = ?::UUID AND collectivity_id = ?::UUID AND occupation != 'JUNIOR' AND end_date IS NULL
                        ORDER BY end_date DESC
                        """);
            ps.setString(1, memberId);
            ps.setString(2, collectivityId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLongTimeMember(String memberId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("""
                        SELECT id, start_date, end_date
                        FROM memberships WHERE member_id = ?::UUID
                        ORDER BY end_date DESC
                        """);
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            long daysAsMember = 0;
            while (rs.next()) {
                if (daysAsMember >= 180) return true;
                var startDate = rs.getDate("start_date").toLocalDate();
                var endDate = (rs.getDate("end_date") == null) ? LocalDate.now() : rs.getDate("start_date").toLocalDate();
                daysAsMember += endDate.toEpochDay() - startDate.toEpochDay();
                System.out.println(daysAsMember);
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MemberResponse> getMemberReferees(String id) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement referalsPs = connection.prepareStatement("SELECT referee_id FROM referals WHERE member_id = ?::UUID;");
            referalsPs.setString(1, id);
            ResultSet referalsRs = referalsPs.executeQuery();
            List<MemberResponse> referals = new ArrayList<>();
            while (referalsRs.next()) {
                referals.add(this.findById(referalsRs.getString("referee_id")));
            }
            return referals;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PositionType getLatestOccupation(String memberId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("""
                        SELECT occupation
                        FROM memberships WHERE member_id = ?::UUID AND end_date IS NULL
                        ORDER BY end_date DESC
                        """);
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return PositionType.valueOf(rs.getString("occupation"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

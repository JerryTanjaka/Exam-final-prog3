package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.collectivity.CollectivityIdentity;
import hei.fprog3.dto.collectivity.CollectivityResponse;
import hei.fprog3.dto.collectivity.CreateCollectivityRequest;
import hei.fprog3.dto.member.MemberResponse;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Member;
import hei.fprog3.model.enums.GenderType;
import hei.fprog3.model.enums.PositionType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollectivityRepository {
    private DataSourceConfig dataSource;
    private MemberRepository memberRepository;
    public  CollectivityRepository(DataSourceConfig dataSource,  MemberRepository memberRepository) {
        this.dataSource = dataSource;
        this.memberRepository =memberRepository;
    }

    public List<CollectivityResponse> create(List<CreateCollectivityRequest> collectivities) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection.toString());
        try {
            List<String> newCollectivitiesId = new ArrayList<>();
            connection.setAutoCommit(false);
            for (CreateCollectivityRequest collectivity : collectivities) {
                PreparedStatement collectivitiesPs = connection.prepareStatement(
                        """
                        INSERT INTO collectivities (city, specialty)
                        VALUES (?, ?)
                        RETURNING id;
                        """
                );

                collectivitiesPs.setString(1, collectivity.getCity());
                collectivitiesPs.setObject(2, collectivity.getSpecialty());
                ResultSet rs = collectivitiesPs.executeQuery();
                while (rs.next()) {
                    newCollectivitiesId.add(rs.getString("id"));
                }

                for (String memberId : collectivity.getMembers()) {
                    PreparedStatement membershipsPs = connection.prepareStatement(
                            """
                            INSERT INTO memberships (member_id, collectivity_id, occupation)
                            VALUES (?, ?, ?::position_type)
                            """
                    );
                    membershipsPs.setString(1, memberId);
                    membershipsPs.setString(2, collectivity.getId());

                    if (memberId.equals(collectivity.getStructure().getPresident())) {
                        membershipsPs.setString(3, PositionType.PRESIDENT.name());
                    } else if (memberId.equals(collectivity.getStructure().getVicePresident())) {
                        membershipsPs.setString(3, PositionType.VICE_PRESIDENT.name());
                    } else if (memberId.equals(collectivity.getStructure().getSecretary())) {
                        membershipsPs.setString(3, PositionType.SECRETARY.name());
                    } else if (memberId.equals(collectivity.getStructure().getTreasurer())) {
                        membershipsPs.setString(3, PositionType.TREASURER.name());
                    } else {
                        membershipsPs.setString(3, PositionType.SENIOR.name());
                    }
                }
            }
            connection.commit();
            List<CollectivityResponse> collectivitiesResponse = new ArrayList<>();
            for (String id : newCollectivitiesId) {
                collectivitiesResponse.add(findById(id));
            }
            return collectivitiesResponse;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public CollectivityResponse findById(String id) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement collectivitiesPs = connection.prepareStatement("""
                        SELECT id, number, name, city, specialty, creation_date
                        FROM collectivities WHERE id = ?::UUID
                        """);
            collectivitiesPs.setString(1, id);

            CollectivityResponse collectivity = new CollectivityResponse();

            ResultSet membersRs = collectivitiesPs.executeQuery();
            if (membersRs.next()) {
                collectivity.setId(membersRs.getString("id"));
                CollectivityIdentity collectivityIdentity = new CollectivityIdentity(membersRs.getString("name"), membersRs.getString("number"));
                collectivity.setIdentity(collectivityIdentity);
                collectivity.setCity(membersRs.getString("city"));
                collectivity.setSpecialty(membersRs.getString("specialty"));
                collectivity.setCreationDate(membersRs.getDate("creation_date").toLocalDate());
            }

            List<Member> members = new ArrayList<>();
            PreparedStatement membershipsPs = connection.prepareStatement("""
                        SELECT id, member_id, occupation
                        FROM memberships WHERE collectivity_id = ?::UUID
                        """);
            membershipsPs.setString(1, id);
            ResultSet membershipsRs = membershipsPs.executeQuery();

            while (membershipsRs.next()) {
                MemberResponse member = memberRepository.findById(membershipsRs.getString("member_id"));
                member.setOccupation(PositionType.valueOf(membershipsRs.getString("occupation")));
                members.add(member);
            }

            collectivity.setMembers(members);
            return collectivity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CollectivityResponse updateCollectivityIdentity(String id, CollectivityIdentity collectivityIdentity) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement collectivitiesPs = connection.prepareStatement(
                    """
                    UPDATE collectivities SET name = ?, number = ?
                    WHERE id = ?
                    """
            );
            collectivitiesPs.setString(1, collectivityIdentity.getName());
            collectivitiesPs.setObject(2, collectivityIdentity.getNumber());
            collectivitiesPs.setString(3, id);
            ResultSet rs = collectivitiesPs.executeQuery();
            connection.commit();
            return findById(id);
        } catch (SQLException e) {
            dataSource.rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public boolean existsByName(String name, String excludeId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(id) FROM collectivities WHERE name = ? AND id != ?::UUID"
            );
            ps.setString(1, name);
            ps.setString(2, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public boolean existsByNumber(int number, String excludeId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(id) FROM collectivities WHERE number = ? AND id != ?::UUID"
            );
            ps.setInt(1, number);
            ps.setString(2, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

}

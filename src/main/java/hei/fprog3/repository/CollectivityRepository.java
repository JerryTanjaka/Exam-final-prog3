package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
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
        try {
            List<String> newCollectivitiesId = new ArrayList<>();
            connection.setAutoCommit(false);
            for (CreateCollectivityRequest collectivity : collectivities) {
                PreparedStatement collectivitiesPs = connection.prepareStatement(
                        """
                        INSERT INTO collectivity (number, name, city, specialty, creation_date)
                        VALUES (?, ?, ?, ?, ?)
                        RETURNING id;
                        """
                );
                collectivitiesPs.setString(1, collectivity.getNumber());
                collectivitiesPs.setString(2, collectivity.getName());
                collectivitiesPs.setString(3, collectivity.getCity());
                collectivitiesPs.setObject(4, collectivity.getSpecialty());
                collectivitiesPs.setDate(5, Date.valueOf(collectivity.getCreationDate()));
                ResultSet rs = collectivitiesPs.executeQuery();
                while (rs.next()) {
                    newCollectivitiesId.add(rs.getString("id"));
                }

                for (String memberId : collectivity.getMembers()) {
                    PreparedStatement membershipsPs = connection.prepareStatement(
                            """
                            INSERT INTO membership (member_id, collectivity_id, occupation, start_date)
                            VALUES (?, ?, ?::position_type, ?)
                            """
                    );
                    membershipsPs.setString(1, memberId);
                    membershipsPs.setString(2, collectivity.getId());

                    if (memberId.equals(collectivity.getStructure().getPresident())) {
                        membershipsPs.setObject(3, PositionType.PRESIDENT);
                    } else if (memberId.equals(collectivity.getStructure().getVicePresident())) {
                        membershipsPs.setObject(3, PositionType.VICE_PRESIDENT);
                    } else if (memberId.equals(collectivity.getStructure().getSecretary())) {
                        membershipsPs.setObject(3, PositionType.SECRETARY);
                    } else if (memberId.equals(collectivity.getStructure().getTreasurer())) {
                        membershipsPs.setObject(3, PositionType.TREASURER);
                    } else {
                        membershipsPs.setObject(3, PositionType.SENIOR);
                    }

                    membershipsPs.setDate(4, Date.valueOf(collectivity.getCreationDate()));
                }
            }
            connection.commit();
            List<CollectivityResponse> collectivitiesResponse = new ArrayList<>();
            for (String id : newCollectivitiesId) {
                collectivitiesResponse.add(findById(id));
            }
            return collectivitiesResponse;
        } catch (SQLException e) {
            dataSource.rollbackConnection(connection);
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
                        FROM collectivity WHERE id = ?
                        """);
            collectivitiesPs.setString(1, id);

            CollectivityResponse collectivity = new CollectivityResponse();

            ResultSet membersRs = collectivitiesPs.executeQuery();
            if (membersRs.next()) {
                collectivity.setId(membersRs.getString("id"));
                collectivity.setNumber(membersRs.getString("number"));
                collectivity.setName(membersRs.getString("name"));
                collectivity.setCity(membersRs.getString("city"));
                collectivity.setSpecialty(membersRs.getString("specialty"));
                collectivity.setCreationDate(membersRs.getDate("creation_date").toLocalDate());
            }

            List<Member> members = new ArrayList<>();
            PreparedStatement membershipsPs = connection.prepareStatement("""
                        SELECT id, member_id, occupation
                        FROM memberships WHERE collectivity_id = ?
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

}

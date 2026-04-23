package hei.fprog3.repository;

import hei.fprog3.datasource.DataSourceConfig;
import hei.fprog3.dto.collectivity.*;
import hei.fprog3.dto.member.MemberResponse;
import hei.fprog3.exception.NotFoundException;
import hei.fprog3.model.Member;
import hei.fprog3.model.enums.PositionType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            connection.setAutoCommit(false);
            List<CollectivityResponse> collectivitiesResponse = new ArrayList<>();

            PreparedStatement collectivitiesPs = connection.prepareStatement(
                    """
                    INSERT INTO collectivities (id, location, specialty)
                    VALUES (?::UUID, ?, ?)
                    """
            );

            PreparedStatement membershipsPs = connection.prepareStatement(
                    """
                    INSERT INTO memberships (member_id, collectivity_id, occupation)
                    VALUES (?::UUID, ?::UUID, ?::position_type)
                    """
            );

            for (CreateCollectivityRequest collectivity : collectivities) {
                collectivity.setId(UUID.randomUUID().toString());
                collectivitiesPs.setString(1, collectivity.getId());
                collectivitiesPs.setString(2, collectivity.getLocation());
                collectivitiesPs.setObject(3, collectivity.getSpecialty());
                collectivitiesPs.addBatch();

                for (String memberId : collectivity.getMembers()) {
                    memberRepository.findById(memberId);

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
                    membershipsPs.addBatch();
                }
            }

            collectivitiesPs.executeBatch();
            membershipsPs.executeBatch();

            connection.commit();
            for (CreateCollectivityRequest collectivity : collectivities) {
                collectivitiesResponse.add(findById(collectivity.getId()));
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
                        SELECT id, number, name, location, specialty, creation_date
                        FROM collectivities WHERE id = ?::UUID
                        """);
            collectivitiesPs.setString(1, id);

            CollectivityResponse collectivity = new CollectivityResponse();

            ResultSet collectivitiesRs = collectivitiesPs.executeQuery();

            if (!collectivitiesRs.next()) {
                throw new NotFoundException("Collectivity not found");
            }

            collectivity.setId(collectivitiesRs.getString("id"));
            CollectivityInformation collectivityInformation = new CollectivityInformation(collectivitiesRs.getString("name"), collectivitiesRs.getString("number"));
            collectivity.setIdentity(collectivityInformation);
            collectivity.setLocation(collectivitiesRs.getString("location"));
            collectivity.setSpecialty(collectivitiesRs.getString("specialty"));
            collectivity.setCreationDate(collectivitiesRs.getDate("creation_date").toLocalDate());

            List<Member> members = new ArrayList<>();
            PreparedStatement membershipsPs = connection.prepareStatement("""
                        SELECT id, member_id, occupation
                        FROM memberships WHERE collectivity_id = ?::UUID
                        """);
            membershipsPs.setString(1, id);
            ResultSet membershipsRs = membershipsPs.executeQuery();

            CollectivityStructureResponse collectivityStructureResponse = new CollectivityStructureResponse();
            while (membershipsRs.next()) {
                MemberResponse member = memberRepository.findById(membershipsRs.getString("member_id"));
                member.setOccupation(PositionType.valueOf(membershipsRs.getString("occupation")));
                if (PositionType.valueOf(membershipsRs.getString("occupation")).equals(PositionType.PRESIDENT)) {
                    collectivityStructureResponse.setPresident(member);
                } else if (PositionType.valueOf(membershipsRs.getString("occupation")).equals(PositionType.VICE_PRESIDENT)) {
                    collectivityStructureResponse.setVicePresident(member);
                } else if (PositionType.valueOf(membershipsRs.getString("occupation")).equals(PositionType.SECRETARY)) {
                    collectivityStructureResponse.setSecretary(member);
                } else if (PositionType.valueOf(membershipsRs.getString("occupation")).equals(PositionType.TREASURER)) {
                    collectivityStructureResponse.setTreasurer(member);
                }
                members.add(member);
            }

            collectivity.setStructure(collectivityStructureResponse);
            collectivity.setMembers(members);
            return collectivity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public void exists(String id) throws NotFoundException {
        Connection  connection = dataSource.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM collectivities WHERE id = ?::UUID");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new NotFoundException("Collectivity with id %s not found".formatted(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.closeConnection(connection);
        }
    }

    public CollectivityResponse updateCollectivityInformation(String id, CollectivityInformation collectivityInformation) throws NotFoundException {
        Connection connection = dataSource.getConnection();
        try {
            exists(id);
            connection.setAutoCommit(false);
            PreparedStatement collectivitiesPs = connection.prepareStatement(
                    """
                    UPDATE collectivities SET name = ?, number = ?
                    WHERE id = ?::UUID
                    """
            );
            collectivitiesPs.setString(1, collectivityInformation.getName());
            collectivitiesPs.setObject(2, collectivityInformation.getNumber());
            collectivitiesPs.setString(3, id);
            collectivitiesPs.executeUpdate();
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

    public boolean existsByNumber(String number, String excludeId) {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT COUNT(id) FROM collectivities WHERE number = ? AND id != ?::UUID"
            );
            ps.setString(1, number);
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

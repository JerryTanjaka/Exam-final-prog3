package hei.fprog3.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Représente un mandat annuel d'une collectivité.
 * Table : mandates
 *
 * Règles métier :
 * - Un mandat = une année civile (startDate = 1er jan, endDate = 31 déc)
 * - Chaque poste spécifique ne peut être occupé qu'une fois par mandat
 * - Un membre ne peut pas occuper le même poste spécifique plus de 2 fois au total
 */
public class Mandate {

    private UUID id;
    private UUID communityId;       // FK → communities.id
    private int year;
    private UUID presidentId;       // FK → members.id
    private UUID vicePresidentId;   // FK → members.id
    private UUID treasurerId;       // FK → members.id
    private UUID secretaryId;       // FK → members.id
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    public Mandate() {}

    public UUID getId() { return id; }
    public UUID getCommunityId() { return communityId; }
    public int getYear() { return year; }
    public UUID getPresidentId() { return presidentId; }
    public UUID getVicePresidentId() { return vicePresidentId; }
    public UUID getTreasurerId() { return treasurerId; }
    public UUID getSecretaryId() { return secretaryId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(UUID id) { this.id = id; }
    public void setCommunityId(UUID communityId) { this.communityId = communityId; }
    public void setYear(int year) { this.year = year; }
    public void setPresidentId(UUID presidentId) { this.presidentId = presidentId; }
    public void setVicePresidentId(UUID vicePresidentId) { this.vicePresidentId = vicePresidentId; }
    public void setTreasurerId(UUID treasurerId) { this.treasurerId = treasurerId; }
    public void setSecretaryId(UUID secretaryId) { this.secretaryId = secretaryId; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

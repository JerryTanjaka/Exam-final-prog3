package hei.fprog3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CommunityChange {

    private UUID id;
    private UUID memberId;
    private UUID oldCommunityId;
    private UUID newCommunityId;
    private String reason;
    private LocalDate changeDate;
    private LocalDateTime createdAt;

    public CommunityChange() {}

    public UUID getId() { return id; }
    public UUID getMemberId() { return memberId; }
    public UUID getOldCommunityId() { return oldCommunityId; }
    public UUID getNewCommunityId() { return newCommunityId; }
    public String getReason() { return reason; }
    public LocalDate getChangeDate() { return changeDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(UUID id) { this.id = id; }
    public void setMemberId(UUID memberId) { this.memberId = memberId; }
    public void setOldCommunityId(UUID oldCommunityId) { this.oldCommunityId = oldCommunityId; }
    public void setNewCommunityId(UUID newCommunityId) { this.newCommunityId = newCommunityId; }
    public void setReason(String reason) { this.reason = reason; }
    public void setChangeDate(LocalDate changeDate) { this.changeDate = changeDate; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.BlockedEntityType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BlockedEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlockedEntityDTO implements Serializable {

    private Long id;

    @NotNull
    private BlockedEntityType entityType;

    @NotNull
    @Size(max = 64)
    private String valueHash;

    @Size(max = 200)
    private String reason;

    private Instant blockedUntil;

    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BlockedEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(BlockedEntityType entityType) {
        this.entityType = entityType;
    }

    public String getValueHash() {
        return valueHash;
    }

    public void setValueHash(String valueHash) {
        this.valueHash = valueHash;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(Instant blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockedEntityDTO)) {
            return false;
        }

        BlockedEntityDTO blockedEntityDTO = (BlockedEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, blockedEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlockedEntityDTO{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", valueHash='" + getValueHash() + "'" +
            ", reason='" + getReason() + "'" +
            ", blockedUntil='" + getBlockedUntil() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

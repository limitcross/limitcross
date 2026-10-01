package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.BlockedEntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A BlockedEntity.
 */
@Entity
@Table(name = "blocked_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BlockedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private BlockedEntityType entityType;

    @NotNull
    @Size(max = 64)
    @Column(name = "value_hash", length = 64, nullable = false)
    private String valueHash;

    @Size(max = 200)
    @Column(name = "reason", length = 200)
    private String reason;

    @Column(name = "blocked_until")
    private Instant blockedUntil;

    @Column(name = "created_at")
    private Instant createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BlockedEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BlockedEntityType getEntityType() {
        return this.entityType;
    }

    public BlockedEntity entityType(BlockedEntityType entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(BlockedEntityType entityType) {
        this.entityType = entityType;
    }

    public String getValueHash() {
        return this.valueHash;
    }

    public BlockedEntity valueHash(String valueHash) {
        this.setValueHash(valueHash);
        return this;
    }

    public void setValueHash(String valueHash) {
        this.valueHash = valueHash;
    }

    public String getReason() {
        return this.reason;
    }

    public BlockedEntity reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getBlockedUntil() {
        return this.blockedUntil;
    }

    public BlockedEntity blockedUntil(Instant blockedUntil) {
        this.setBlockedUntil(blockedUntil);
        return this;
    }

    public void setBlockedUntil(Instant blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BlockedEntity createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockedEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((BlockedEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlockedEntity{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", valueHash='" + getValueHash() + "'" +
            ", reason='" + getReason() + "'" +
            ", blockedUntil='" + getBlockedUntil() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

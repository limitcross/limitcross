package com.limitcross.facility.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A AuditLog.
 */
@Entity
@Table(name = "audit_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "actor", length = 50, nullable = false)
    private String actor;

    @NotNull
    @Size(max = 40)
    @Column(name = "action", length = 40, nullable = false)
    private String action;

    @NotNull
    @Size(max = 40)
    @Column(name = "entity_type", length = 40, nullable = false)
    private String entityType;

    @NotNull
    @Size(max = 64)
    @Column(name = "entity_id", length = 64, nullable = false)
    private String entityId;

    @Lob
    @Column(name = "old_value")
    private String oldValue;

    @Lob
    @Column(name = "new_value")
    private String newValue;

    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "created_at")
    private Instant createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AuditLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActor() {
        return this.actor;
    }

    public AuditLog actor(String actor) {
        this.setActor(actor);
        return this;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAction() {
        return this.action;
    }

    public AuditLog action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public AuditLog entityType(String entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public AuditLog entityId(String entityId) {
        this.setEntityId(entityId);
        return this;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getOldValue() {
        return this.oldValue;
    }

    public AuditLog oldValue(String oldValue) {
        this.setOldValue(oldValue);
        return this;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public AuditLog newValue(String newValue) {
        this.setNewValue(newValue);
        return this;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public AuditLog ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public AuditLog createdAt(Instant createdAt) {
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
        if (!(o instanceof AuditLog)) {
            return false;
        }
        return getId() != null && getId().equals(((AuditLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLog{" +
            "id=" + getId() +
            ", actor='" + getActor() + "'" +
            ", action='" + getAction() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", entityId='" + getEntityId() + "'" +
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

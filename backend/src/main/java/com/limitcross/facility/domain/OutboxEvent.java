package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.OutboxStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A OutboxEvent.
 */
@Entity
@Table(name = "outbox_event")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OutboxEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "aggregate_type", length = 40, nullable = false)
    private String aggregateType;

    @NotNull
    @Size(max = 64)
    @Column(name = "aggregate_id", length = 64, nullable = false)
    private String aggregateId;

    @NotNull
    @Size(max = 60)
    @Column(name = "event_type", length = 60, nullable = false)
    private String eventType;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboxStatus status;

    @Min(value = 0)
    @Column(name = "attempts")
    private Integer attempts;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OutboxEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAggregateType() {
        return this.aggregateType;
    }

    public OutboxEvent aggregateType(String aggregateType) {
        this.setAggregateType(aggregateType);
        return this;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public String getAggregateId() {
        return this.aggregateId;
    }

    public OutboxEvent aggregateId(String aggregateId) {
        this.setAggregateId(aggregateId);
        return this;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return this.eventType;
    }

    public OutboxEvent eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return this.payload;
    }

    public OutboxEvent payload(String payload) {
        this.setPayload(payload);
        return this;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public OutboxStatus getStatus() {
        return this.status;
    }

    public OutboxEvent status(OutboxStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OutboxStatus status) {
        this.status = status;
    }

    public Integer getAttempts() {
        return this.attempts;
    }

    public OutboxEvent attempts(Integer attempts) {
        this.setAttempts(attempts);
        return this;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public OutboxEvent createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getProcessedAt() {
        return this.processedAt;
    }

    public OutboxEvent processedAt(Instant processedAt) {
        this.setProcessedAt(processedAt);
        return this;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutboxEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((OutboxEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutboxEvent{" +
            "id=" + getId() +
            ", aggregateType='" + getAggregateType() + "'" +
            ", aggregateId='" + getAggregateId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", payload='" + getPayload() + "'" +
            ", status='" + getStatus() + "'" +
            ", attempts=" + getAttempts() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            "}";
    }
}

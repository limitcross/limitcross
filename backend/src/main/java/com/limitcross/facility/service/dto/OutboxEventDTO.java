package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.OutboxStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.OutboxEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OutboxEventDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String aggregateType;

    @NotNull
    @Size(max = 64)
    private String aggregateId;

    @NotNull
    @Size(max = 60)
    private String eventType;

    @Lob
    private String payload;

    @NotNull
    private OutboxStatus status;

    @Min(value = 0)
    private Integer attempts;

    private Instant createdAt;

    private Instant processedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public void setStatus(OutboxStatus status) {
        this.status = status;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutboxEventDTO)) {
            return false;
        }

        OutboxEventDTO outboxEventDTO = (OutboxEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outboxEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutboxEventDTO{" +
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

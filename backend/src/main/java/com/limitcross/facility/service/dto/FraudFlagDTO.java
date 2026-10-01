package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.FraudEntityType;
import com.limitcross.facility.domain.enumeration.FraudStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.FraudFlag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FraudFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private FraudEntityType entityType;

    @NotNull
    @Size(max = 64)
    private String entityId;

    @NotNull
    @Size(max = 40)
    private String ruleCode;

    @Min(value = 0)
    private Integer riskScore;

    @NotNull
    private FraudStatus status;

    @Lob
    private String details;

    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FraudEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(FraudEntityType entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public FraudStatus getStatus() {
        return status;
    }

    public void setStatus(FraudStatus status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
        if (!(o instanceof FraudFlagDTO)) {
            return false;
        }

        FraudFlagDTO fraudFlagDTO = (FraudFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fraudFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FraudFlagDTO{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", entityId='" + getEntityId() + "'" +
            ", ruleCode='" + getRuleCode() + "'" +
            ", riskScore=" + getRiskScore() +
            ", status='" + getStatus() + "'" +
            ", details='" + getDetails() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

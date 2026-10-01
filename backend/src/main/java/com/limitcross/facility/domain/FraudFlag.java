package com.limitcross.facility.domain;

import com.limitcross.facility.domain.enumeration.FraudEntityType;
import com.limitcross.facility.domain.enumeration.FraudStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A FraudFlag.
 */
@Entity
@Table(name = "fraud_flag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FraudFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private FraudEntityType entityType;

    @NotNull
    @Size(max = 64)
    @Column(name = "entity_id", length = 64, nullable = false)
    private String entityId;

    @NotNull
    @Size(max = 40)
    @Column(name = "rule_code", length = 40, nullable = false)
    private String ruleCode;

    @Min(value = 0)
    @Column(name = "risk_score")
    private Integer riskScore;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FraudStatus status;

    @Lob
    @Column(name = "details")
    private String details;

    @Column(name = "created_at")
    private Instant createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FraudFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FraudEntityType getEntityType() {
        return this.entityType;
    }

    public FraudFlag entityType(FraudEntityType entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(FraudEntityType entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public FraudFlag entityId(String entityId) {
        this.setEntityId(entityId);
        return this;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getRuleCode() {
        return this.ruleCode;
    }

    public FraudFlag ruleCode(String ruleCode) {
        this.setRuleCode(ruleCode);
        return this;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public Integer getRiskScore() {
        return this.riskScore;
    }

    public FraudFlag riskScore(Integer riskScore) {
        this.setRiskScore(riskScore);
        return this;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public FraudStatus getStatus() {
        return this.status;
    }

    public FraudFlag status(FraudStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(FraudStatus status) {
        this.status = status;
    }

    public String getDetails() {
        return this.details;
    }

    public FraudFlag details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public FraudFlag createdAt(Instant createdAt) {
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
        if (!(o instanceof FraudFlag)) {
            return false;
        }
        return getId() != null && getId().equals(((FraudFlag) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FraudFlag{" +
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

package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A ProfessionalIncentiveAward.
 */
@Entity
@Table(name = "professional_incentive_award")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalIncentiveAward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @NotNull
    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "city" }, allowSetters = true)
    private IncentiveRule rule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "professional", "booking" }, allowSetters = true)
    private WalletTransaction walletTransaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalIncentiveAward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPeriodStart() {
        return this.periodStart;
    }

    public ProfessionalIncentiveAward periodStart(LocalDate periodStart) {
        this.setPeriodStart(periodStart);
        return this;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return this.periodEnd;
    }

    public ProfessionalIncentiveAward periodEnd(LocalDate periodEnd) {
        this.setPeriodEnd(periodEnd);
        return this;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public ProfessionalIncentiveAward amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ProfessionalIncentiveAward createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ProfessionalIncentiveAward professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    public IncentiveRule getRule() {
        return this.rule;
    }

    public void setRule(IncentiveRule incentiveRule) {
        this.rule = incentiveRule;
    }

    public ProfessionalIncentiveAward rule(IncentiveRule incentiveRule) {
        this.setRule(incentiveRule);
        return this;
    }

    public WalletTransaction getWalletTransaction() {
        return this.walletTransaction;
    }

    public void setWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransaction = walletTransaction;
    }

    public ProfessionalIncentiveAward walletTransaction(WalletTransaction walletTransaction) {
        this.setWalletTransaction(walletTransaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalIncentiveAward)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalIncentiveAward) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalIncentiveAward{" +
            "id=" + getId() +
            ", periodStart='" + getPeriodStart() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", amount=" + getAmount() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

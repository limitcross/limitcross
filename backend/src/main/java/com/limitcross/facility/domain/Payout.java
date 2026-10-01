package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.PayoutStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Payout.
 */
@Entity
@Table(name = "payout")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayoutStatus status;

    @Size(max = 80)
    @Column(name = "bank_reference", length = 80)
    private String bankReference;

    @NotNull
    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @NotNull
    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payout amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PayoutStatus getStatus() {
        return this.status;
    }

    public Payout status(PayoutStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PayoutStatus status) {
        this.status = status;
    }

    public String getBankReference() {
        return this.bankReference;
    }

    public Payout bankReference(String bankReference) {
        this.setBankReference(bankReference);
        return this;
    }

    public void setBankReference(String bankReference) {
        this.bankReference = bankReference;
    }

    public LocalDate getPeriodStart() {
        return this.periodStart;
    }

    public Payout periodStart(LocalDate periodStart) {
        this.setPeriodStart(periodStart);
        return this;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return this.periodEnd;
    }

    public Payout periodEnd(LocalDate periodEnd) {
        this.setPeriodEnd(periodEnd);
        return this;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Payout createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getPaidAt() {
        return this.paidAt;
    }

    public Payout paidAt(Instant paidAt) {
        this.setPaidAt(paidAt);
        return this;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public Payout professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payout)) {
            return false;
        }
        return getId() != null && getId().equals(((Payout) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payout{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", bankReference='" + getBankReference() + "'" +
            ", periodStart='" + getPeriodStart() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", paidAt='" + getPaidAt() + "'" +
            "}";
    }
}

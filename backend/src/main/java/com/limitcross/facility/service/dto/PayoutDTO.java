package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.PayoutStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.Payout} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PayoutDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    @NotNull
    private PayoutStatus status;

    @Size(max = 80)
    private String bankReference;

    @NotNull
    private LocalDate periodStart;

    @NotNull
    private LocalDate periodEnd;

    private Instant createdAt;

    private Instant paidAt;

    @NotNull
    private ProfessionalDTO professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PayoutStatus getStatus() {
        return status;
    }

    public void setStatus(PayoutStatus status) {
        this.status = status;
    }

    public String getBankReference() {
        return bankReference;
    }

    public void setBankReference(String bankReference) {
        this.bankReference = bankReference;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayoutDTO)) {
            return false;
        }

        PayoutDTO payoutDTO = (PayoutDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, payoutDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PayoutDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", bankReference='" + getBankReference() + "'" +
            ", periodStart='" + getPeriodStart() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", paidAt='" + getPaidAt() + "'" +
            ", professional=" + getProfessional() +
            "}";
    }
}

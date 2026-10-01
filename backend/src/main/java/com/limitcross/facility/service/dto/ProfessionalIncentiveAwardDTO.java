package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalIncentiveAward} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalIncentiveAwardDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate periodStart;

    @NotNull
    private LocalDate periodEnd;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    private Instant createdAt;

    @NotNull
    private ProfessionalDTO professional;

    @NotNull
    private IncentiveRuleDTO rule;

    private WalletTransactionDTO walletTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    public IncentiveRuleDTO getRule() {
        return rule;
    }

    public void setRule(IncentiveRuleDTO rule) {
        this.rule = rule;
    }

    public WalletTransactionDTO getWalletTransaction() {
        return walletTransaction;
    }

    public void setWalletTransaction(WalletTransactionDTO walletTransaction) {
        this.walletTransaction = walletTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalIncentiveAwardDTO)) {
            return false;
        }

        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = (ProfessionalIncentiveAwardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalIncentiveAwardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalIncentiveAwardDTO{" +
            "id=" + getId() +
            ", periodStart='" + getPeriodStart() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", amount=" + getAmount() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", professional=" + getProfessional() +
            ", rule=" + getRule() +
            ", walletTransaction=" + getWalletTransaction() +
            "}";
    }
}

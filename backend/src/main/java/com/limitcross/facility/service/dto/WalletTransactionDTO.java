package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.WalletTxnType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.WalletTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private WalletTxnType txnType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal balanceAfter;

    @Size(max = 255)
    private String description;

    @NotNull
    private Instant createdAt;

    @NotNull
    private ProfessionalDTO professional;

    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WalletTxnType getTxnType() {
        return txnType;
    }

    public void setTxnType(WalletTxnType txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WalletTransactionDTO)) {
            return false;
        }

        WalletTransactionDTO walletTransactionDTO = (WalletTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, walletTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletTransactionDTO{" +
            "id=" + getId() +
            ", txnType='" + getTxnType() + "'" +
            ", amount=" + getAmount() +
            ", balanceAfter=" + getBalanceAfter() +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", professional=" + getProfessional() +
            ", booking=" + getBooking() +
            "}";
    }
}

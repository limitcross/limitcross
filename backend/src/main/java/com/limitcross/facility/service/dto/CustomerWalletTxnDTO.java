package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.CustomerWalletTxnType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CustomerWalletTxn} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerWalletTxnDTO implements Serializable {

    private Long id;

    @NotNull
    private CustomerWalletTxnType txnType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal balanceAfter;

    private Instant expiresAt;

    @Size(max = 200)
    private String note;

    @NotNull
    private Instant createdAt;

    @NotNull
    private UserDTO user;

    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerWalletTxnType getTxnType() {
        return txnType;
    }

    public void setTxnType(CustomerWalletTxnType txnType) {
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

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
        if (!(o instanceof CustomerWalletTxnDTO)) {
            return false;
        }

        CustomerWalletTxnDTO customerWalletTxnDTO = (CustomerWalletTxnDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerWalletTxnDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerWalletTxnDTO{" +
            "id=" + getId() +
            ", txnType='" + getTxnType() + "'" +
            ", amount=" + getAmount() +
            ", balanceAfter=" + getBalanceAfter() +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", note='" + getNote() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", user=" + getUser() +
            ", booking=" + getBooking() +
            "}";
    }
}

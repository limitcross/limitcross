package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.CustomerWalletTxnType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A CustomerWalletTxn.
 */
@Entity
@Table(name = "customer_wallet_txn")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerWalletTxn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "txn_type", nullable = false)
    private CustomerWalletTxnType txnType;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "balance_after", precision = 21, scale = 2, nullable = false)
    private BigDecimal balanceAfter;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Size(max = 200)
    @Column(name = "note", length = 200)
    private String note;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "items",
            "statusHistories",
            "assignments",
            "media",
            "payments",
            "quotes",
            "reschedules",
            "customer",
            "service",
            "city",
            "address",
            "professional",
            "coupon",
            "subscription",
            "slotCapacity",
            "couponRedemption",
            "review",
            "chatThread",
        },
        allowSetters = true
    )
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerWalletTxn id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerWalletTxnType getTxnType() {
        return this.txnType;
    }

    public CustomerWalletTxn txnType(CustomerWalletTxnType txnType) {
        this.setTxnType(txnType);
        return this;
    }

    public void setTxnType(CustomerWalletTxnType txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public CustomerWalletTxn amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceAfter() {
        return this.balanceAfter;
    }

    public CustomerWalletTxn balanceAfter(BigDecimal balanceAfter) {
        this.setBalanceAfter(balanceAfter);
        return this;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public CustomerWalletTxn expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getNote() {
        return this.note;
    }

    public CustomerWalletTxn note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CustomerWalletTxn createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CustomerWalletTxn user(User user) {
        this.setUser(user);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public CustomerWalletTxn booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerWalletTxn)) {
            return false;
        }
        return getId() != null && getId().equals(((CustomerWalletTxn) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerWalletTxn{" +
            "id=" + getId() +
            ", txnType='" + getTxnType() + "'" +
            ", amount=" + getAmount() +
            ", balanceAfter=" + getBalanceAfter() +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", note='" + getNote() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

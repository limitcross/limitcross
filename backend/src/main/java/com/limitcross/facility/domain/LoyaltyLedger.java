package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.LoyaltyReason;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A LoyaltyLedger.
 */
@Entity
@Table(name = "loyalty_ledger")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoyaltyLedger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private LoyaltyReason reason;

    @Column(name = "expires_at")
    private LocalDate expiresAt;

    @Column(name = "created_at")
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

    public LoyaltyLedger id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return this.points;
    }

    public LoyaltyLedger points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LoyaltyReason getReason() {
        return this.reason;
    }

    public LoyaltyLedger reason(LoyaltyReason reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(LoyaltyReason reason) {
        this.reason = reason;
    }

    public LocalDate getExpiresAt() {
        return this.expiresAt;
    }

    public LoyaltyLedger expiresAt(LocalDate expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public LoyaltyLedger createdAt(Instant createdAt) {
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

    public LoyaltyLedger user(User user) {
        this.setUser(user);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public LoyaltyLedger booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoyaltyLedger)) {
            return false;
        }
        return getId() != null && getId().equals(((LoyaltyLedger) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyLedger{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", reason='" + getReason() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

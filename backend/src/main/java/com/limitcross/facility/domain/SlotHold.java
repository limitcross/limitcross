package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.HoldStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A SlotHold.
 */
@Entity
@Table(name = "slot_hold")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SlotHold implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HoldStatus status;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "zone", "category" }, allowSetters = true)
    private SlotCapacity slotCapacity;

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

    public SlotHold id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HoldStatus getStatus() {
        return this.status;
    }

    public SlotHold status(HoldStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(HoldStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public SlotHold expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SlotHold createdAt(Instant createdAt) {
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

    public SlotHold user(User user) {
        this.setUser(user);
        return this;
    }

    public SlotCapacity getSlotCapacity() {
        return this.slotCapacity;
    }

    public void setSlotCapacity(SlotCapacity slotCapacity) {
        this.slotCapacity = slotCapacity;
    }

    public SlotHold slotCapacity(SlotCapacity slotCapacity) {
        this.setSlotCapacity(slotCapacity);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public SlotHold booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlotHold)) {
            return false;
        }
        return getId() != null && getId().equals(((SlotHold) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotHold{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

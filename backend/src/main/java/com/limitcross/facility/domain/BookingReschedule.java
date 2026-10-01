package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.ActorType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A BookingReschedule.
 */
@Entity
@Table(name = "booking_reschedule")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingReschedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "old_start", nullable = false)
    private Instant oldStart;

    @NotNull
    @Column(name = "old_end", nullable = false)
    private Instant oldEnd;

    @NotNull
    @Column(name = "new_start", nullable = false)
    private Instant newStart;

    @NotNull
    @Column(name = "new_end", nullable = false)
    private Instant newEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "requested_by", nullable = false)
    private ActorType requestedBy;

    @Size(max = 255)
    @Column(name = "reason", length = 255)
    private String reason;

    @DecimalMin(value = "0")
    @Column(name = "fee_charged", precision = 21, scale = 2)
    private BigDecimal feeCharged;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
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

    public BookingReschedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOldStart() {
        return this.oldStart;
    }

    public BookingReschedule oldStart(Instant oldStart) {
        this.setOldStart(oldStart);
        return this;
    }

    public void setOldStart(Instant oldStart) {
        this.oldStart = oldStart;
    }

    public Instant getOldEnd() {
        return this.oldEnd;
    }

    public BookingReschedule oldEnd(Instant oldEnd) {
        this.setOldEnd(oldEnd);
        return this;
    }

    public void setOldEnd(Instant oldEnd) {
        this.oldEnd = oldEnd;
    }

    public Instant getNewStart() {
        return this.newStart;
    }

    public BookingReschedule newStart(Instant newStart) {
        this.setNewStart(newStart);
        return this;
    }

    public void setNewStart(Instant newStart) {
        this.newStart = newStart;
    }

    public Instant getNewEnd() {
        return this.newEnd;
    }

    public BookingReschedule newEnd(Instant newEnd) {
        this.setNewEnd(newEnd);
        return this;
    }

    public void setNewEnd(Instant newEnd) {
        this.newEnd = newEnd;
    }

    public ActorType getRequestedBy() {
        return this.requestedBy;
    }

    public BookingReschedule requestedBy(ActorType requestedBy) {
        this.setRequestedBy(requestedBy);
        return this;
    }

    public void setRequestedBy(ActorType requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getReason() {
        return this.reason;
    }

    public BookingReschedule reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getFeeCharged() {
        return this.feeCharged;
    }

    public BookingReschedule feeCharged(BigDecimal feeCharged) {
        this.setFeeCharged(feeCharged);
        return this;
    }

    public void setFeeCharged(BigDecimal feeCharged) {
        this.feeCharged = feeCharged;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BookingReschedule createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BookingReschedule booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingReschedule)) {
            return false;
        }
        return getId() != null && getId().equals(((BookingReschedule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingReschedule{" +
            "id=" + getId() +
            ", oldStart='" + getOldStart() + "'" +
            ", oldEnd='" + getOldEnd() + "'" +
            ", newStart='" + getNewStart() + "'" +
            ", newEnd='" + getNewEnd() + "'" +
            ", requestedBy='" + getRequestedBy() + "'" +
            ", reason='" + getReason() + "'" +
            ", feeCharged=" + getFeeCharged() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

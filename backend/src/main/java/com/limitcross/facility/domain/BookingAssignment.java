package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.AssignmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A BookingAssignment.
 */
@Entity
@Table(name = "booking_assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssignmentStatus status;

    @Column(name = "offered_at")
    private Instant offeredAt;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Size(max = 255)
    @Column(name = "reject_reason", length = 255)
    private String rejectReason;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

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

    public BookingAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssignmentStatus getStatus() {
        return this.status;
    }

    public BookingAssignment status(AssignmentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    public Instant getOfferedAt() {
        return this.offeredAt;
    }

    public BookingAssignment offeredAt(Instant offeredAt) {
        this.setOfferedAt(offeredAt);
        return this;
    }

    public void setOfferedAt(Instant offeredAt) {
        this.offeredAt = offeredAt;
    }

    public Instant getRespondedAt() {
        return this.respondedAt;
    }

    public BookingAssignment respondedAt(Instant respondedAt) {
        this.setRespondedAt(respondedAt);
        return this;
    }

    public void setRespondedAt(Instant respondedAt) {
        this.respondedAt = respondedAt;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public BookingAssignment expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRejectReason() {
        return this.rejectReason;
    }

    public BookingAssignment rejectReason(String rejectReason) {
        this.setRejectReason(rejectReason);
        return this;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public BookingAssignment professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BookingAssignment booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((BookingAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingAssignment{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", offeredAt='" + getOfferedAt() + "'" +
            ", respondedAt='" + getRespondedAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", rejectReason='" + getRejectReason() + "'" +
            "}";
    }
}

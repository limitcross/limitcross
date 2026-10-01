package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.TimeOffReason;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ProfessionalTimeOff.
 */
@Entity
@Table(name = "professional_time_off")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalTimeOff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @NotNull
    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private TimeOffReason reason;

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

    public ProfessionalTimeOff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartsAt() {
        return this.startsAt;
    }

    public ProfessionalTimeOff startsAt(Instant startsAt) {
        this.setStartsAt(startsAt);
        return this;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return this.endsAt;
    }

    public ProfessionalTimeOff endsAt(Instant endsAt) {
        this.setEndsAt(endsAt);
        return this;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public TimeOffReason getReason() {
        return this.reason;
    }

    public ProfessionalTimeOff reason(TimeOffReason reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(TimeOffReason reason) {
        this.reason = reason;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public ProfessionalTimeOff booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ProfessionalTimeOff professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalTimeOff)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalTimeOff) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalTimeOff{" +
            "id=" + getId() +
            ", startsAt='" + getStartsAt() + "'" +
            ", endsAt='" + getEndsAt() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}

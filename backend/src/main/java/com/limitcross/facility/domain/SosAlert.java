package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.SosStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A SosAlert.
 */
@Entity
@Table(name = "sos_alert")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SosAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SosStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User raisedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private User handledBy;

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

    public SosAlert id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public SosAlert latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public SosAlert longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public SosStatus getStatus() {
        return this.status;
    }

    public SosAlert status(SosStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SosStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SosAlert createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getResolvedAt() {
        return this.resolvedAt;
    }

    public SosAlert resolvedAt(Instant resolvedAt) {
        this.setResolvedAt(resolvedAt);
        return this;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public User getRaisedBy() {
        return this.raisedBy;
    }

    public void setRaisedBy(User user) {
        this.raisedBy = user;
    }

    public SosAlert raisedBy(User user) {
        this.setRaisedBy(user);
        return this;
    }

    public User getHandledBy() {
        return this.handledBy;
    }

    public void setHandledBy(User user) {
        this.handledBy = user;
    }

    public SosAlert handledBy(User user) {
        this.setHandledBy(user);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public SosAlert booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SosAlert)) {
            return false;
        }
        return getId() != null && getId().equals(((SosAlert) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SosAlert{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", resolvedAt='" + getResolvedAt() + "'" +
            "}";
    }
}

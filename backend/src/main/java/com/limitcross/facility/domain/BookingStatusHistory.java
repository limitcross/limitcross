package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A BookingStatusHistory.
 */
@Entity
@Table(name = "booking_status_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingStatusHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private BookingStatus fromStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    private BookingStatus toStatus;

    @Size(max = 50)
    @Column(name = "changed_by", length = 50)
    private String changedBy;

    @Size(max = 255)
    @Column(name = "note", length = 255)
    private String note;

    @NotNull
    @Column(name = "created_at", nullable = false)
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

    public BookingStatusHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingStatus getFromStatus() {
        return this.fromStatus;
    }

    public BookingStatusHistory fromStatus(BookingStatus fromStatus) {
        this.setFromStatus(fromStatus);
        return this;
    }

    public void setFromStatus(BookingStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public BookingStatus getToStatus() {
        return this.toStatus;
    }

    public BookingStatusHistory toStatus(BookingStatus toStatus) {
        this.setToStatus(toStatus);
        return this;
    }

    public void setToStatus(BookingStatus toStatus) {
        this.toStatus = toStatus;
    }

    public String getChangedBy() {
        return this.changedBy;
    }

    public BookingStatusHistory changedBy(String changedBy) {
        this.setChangedBy(changedBy);
        return this;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getNote() {
        return this.note;
    }

    public BookingStatusHistory note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BookingStatusHistory createdAt(Instant createdAt) {
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

    public BookingStatusHistory booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingStatusHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((BookingStatusHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingStatusHistory{" +
            "id=" + getId() +
            ", fromStatus='" + getFromStatus() + "'" +
            ", toStatus='" + getToStatus() + "'" +
            ", changedBy='" + getChangedBy() + "'" +
            ", note='" + getNote() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

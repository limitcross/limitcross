package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.MediaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A BookingMedia.
 */
@Entity
@Table(name = "booking_media")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;

    @NotNull
    @Size(max = 255)
    @Column(name = "file_url", length = 255, nullable = false)
    private String fileUrl;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User uploadedBy;

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

    public BookingMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public BookingMedia mediaType(MediaType mediaType) {
        this.setMediaType(mediaType);
        return this;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public BookingMedia fileUrl(String fileUrl) {
        this.setFileUrl(fileUrl);
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BookingMedia createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(User user) {
        this.uploadedBy = user;
    }

    public BookingMedia uploadedBy(User user) {
        this.setUploadedBy(user);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BookingMedia booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((BookingMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingMedia{" +
            "id=" + getId() +
            ", mediaType='" + getMediaType() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

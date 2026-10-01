package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.WarrantyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A WarrantyClaim.
 */
@Entity
@Table(name = "warranty_claim")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarrantyClaim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "issue", length = 1000, nullable = false)
    private String issue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WarrantyStatus status;

    @NotNull
    @Column(name = "within_warranty", nullable = false)
    private Boolean withinWarranty;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User customer;

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
    private Booking redoBooking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WarrantyClaim id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssue() {
        return this.issue;
    }

    public WarrantyClaim issue(String issue) {
        this.setIssue(issue);
        return this;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public WarrantyStatus getStatus() {
        return this.status;
    }

    public WarrantyClaim status(WarrantyStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WarrantyStatus status) {
        this.status = status;
    }

    public Boolean getWithinWarranty() {
        return this.withinWarranty;
    }

    public WarrantyClaim withinWarranty(Boolean withinWarranty) {
        this.setWithinWarranty(withinWarranty);
        return this;
    }

    public void setWithinWarranty(Boolean withinWarranty) {
        this.withinWarranty = withinWarranty;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public WarrantyClaim createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getResolvedAt() {
        return this.resolvedAt;
    }

    public WarrantyClaim resolvedAt(Instant resolvedAt) {
        this.setResolvedAt(resolvedAt);
        return this;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public User getCustomer() {
        return this.customer;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public WarrantyClaim customer(User user) {
        this.setCustomer(user);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public WarrantyClaim booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    public Booking getRedoBooking() {
        return this.redoBooking;
    }

    public void setRedoBooking(Booking booking) {
        this.redoBooking = booking;
    }

    public WarrantyClaim redoBooking(Booking booking) {
        this.setRedoBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarrantyClaim)) {
            return false;
        }
        return getId() != null && getId().equals(((WarrantyClaim) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarrantyClaim{" +
            "id=" + getId() +
            ", issue='" + getIssue() + "'" +
            ", status='" + getStatus() + "'" +
            ", withinWarranty='" + getWithinWarranty() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", resolvedAt='" + getResolvedAt() + "'" +
            "}";
    }
}

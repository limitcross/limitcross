package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.WarrantyStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.WarrantyClaim} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarrantyClaimDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 1000)
    private String issue;

    @NotNull
    private WarrantyStatus status;

    @NotNull
    private Boolean withinWarranty;

    private Instant createdAt;

    private Instant resolvedAt;

    @NotNull
    private UserDTO customer;

    @NotNull
    private BookingDTO booking;

    private BookingDTO redoBooking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public WarrantyStatus getStatus() {
        return status;
    }

    public void setStatus(WarrantyStatus status) {
        this.status = status;
    }

    public Boolean getWithinWarranty() {
        return withinWarranty;
    }

    public void setWithinWarranty(Boolean withinWarranty) {
        this.withinWarranty = withinWarranty;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public UserDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserDTO customer) {
        this.customer = customer;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    public BookingDTO getRedoBooking() {
        return redoBooking;
    }

    public void setRedoBooking(BookingDTO redoBooking) {
        this.redoBooking = redoBooking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarrantyClaimDTO)) {
            return false;
        }

        WarrantyClaimDTO warrantyClaimDTO = (WarrantyClaimDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, warrantyClaimDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarrantyClaimDTO{" +
            "id=" + getId() +
            ", issue='" + getIssue() + "'" +
            ", status='" + getStatus() + "'" +
            ", withinWarranty='" + getWithinWarranty() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", resolvedAt='" + getResolvedAt() + "'" +
            ", customer=" + getCustomer() +
            ", booking=" + getBooking() +
            ", redoBooking=" + getRedoBooking() +
            "}";
    }
}

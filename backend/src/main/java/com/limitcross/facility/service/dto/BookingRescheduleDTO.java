package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.ActorType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BookingReschedule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingRescheduleDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant oldStart;

    @NotNull
    private Instant oldEnd;

    @NotNull
    private Instant newStart;

    @NotNull
    private Instant newEnd;

    @NotNull
    private ActorType requestedBy;

    @Size(max = 255)
    private String reason;

    @DecimalMin(value = "0")
    private BigDecimal feeCharged;

    private Instant createdAt;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOldStart() {
        return oldStart;
    }

    public void setOldStart(Instant oldStart) {
        this.oldStart = oldStart;
    }

    public Instant getOldEnd() {
        return oldEnd;
    }

    public void setOldEnd(Instant oldEnd) {
        this.oldEnd = oldEnd;
    }

    public Instant getNewStart() {
        return newStart;
    }

    public void setNewStart(Instant newStart) {
        this.newStart = newStart;
    }

    public Instant getNewEnd() {
        return newEnd;
    }

    public void setNewEnd(Instant newEnd) {
        this.newEnd = newEnd;
    }

    public ActorType getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(ActorType requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getFeeCharged() {
        return feeCharged;
    }

    public void setFeeCharged(BigDecimal feeCharged) {
        this.feeCharged = feeCharged;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingRescheduleDTO)) {
            return false;
        }

        BookingRescheduleDTO bookingRescheduleDTO = (BookingRescheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingRescheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingRescheduleDTO{" +
            "id=" + getId() +
            ", oldStart='" + getOldStart() + "'" +
            ", oldEnd='" + getOldEnd() + "'" +
            ", newStart='" + getNewStart() + "'" +
            ", newEnd='" + getNewEnd() + "'" +
            ", requestedBy='" + getRequestedBy() + "'" +
            ", reason='" + getReason() + "'" +
            ", feeCharged=" + getFeeCharged() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", booking=" + getBooking() +
            "}";
    }
}

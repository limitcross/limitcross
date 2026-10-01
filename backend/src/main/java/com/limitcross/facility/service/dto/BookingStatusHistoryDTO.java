package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.BookingStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BookingStatusHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingStatusHistoryDTO implements Serializable {

    private Long id;

    private BookingStatus fromStatus;

    @NotNull
    private BookingStatus toStatus;

    @Size(max = 50)
    private String changedBy;

    @Size(max = 255)
    private String note;

    @NotNull
    private Instant createdAt;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(BookingStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public BookingStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(BookingStatus toStatus) {
        this.toStatus = toStatus;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        if (!(o instanceof BookingStatusHistoryDTO)) {
            return false;
        }

        BookingStatusHistoryDTO bookingStatusHistoryDTO = (BookingStatusHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingStatusHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingStatusHistoryDTO{" +
            "id=" + getId() +
            ", fromStatus='" + getFromStatus() + "'" +
            ", toStatus='" + getToStatus() + "'" +
            ", changedBy='" + getChangedBy() + "'" +
            ", note='" + getNote() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", booking=" + getBooking() +
            "}";
    }
}

package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.AssignmentStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BookingAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private AssignmentStatus status;

    private Instant offeredAt;

    private Instant respondedAt;

    @NotNull
    private Instant expiresAt;

    @Size(max = 255)
    private String rejectReason;

    @NotNull
    private ProfessionalDTO professional;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    public Instant getOfferedAt() {
        return offeredAt;
    }

    public void setOfferedAt(Instant offeredAt) {
        this.offeredAt = offeredAt;
    }

    public Instant getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(Instant respondedAt) {
        this.respondedAt = respondedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
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
        if (!(o instanceof BookingAssignmentDTO)) {
            return false;
        }

        BookingAssignmentDTO bookingAssignmentDTO = (BookingAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingAssignmentDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", offeredAt='" + getOfferedAt() + "'" +
            ", respondedAt='" + getRespondedAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", rejectReason='" + getRejectReason() + "'" +
            ", professional=" + getProfessional() +
            ", booking=" + getBooking() +
            "}";
    }
}

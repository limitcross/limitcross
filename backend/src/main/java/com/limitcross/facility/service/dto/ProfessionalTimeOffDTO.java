package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.TimeOffReason;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalTimeOff} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalTimeOffDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant startsAt;

    @NotNull
    private Instant endsAt;

    @NotNull
    private TimeOffReason reason;

    private BookingDTO booking;

    @NotNull
    private ProfessionalDTO professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public TimeOffReason getReason() {
        return reason;
    }

    public void setReason(TimeOffReason reason) {
        this.reason = reason;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalTimeOffDTO)) {
            return false;
        }

        ProfessionalTimeOffDTO professionalTimeOffDTO = (ProfessionalTimeOffDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalTimeOffDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalTimeOffDTO{" +
            "id=" + getId() +
            ", startsAt='" + getStartsAt() + "'" +
            ", endsAt='" + getEndsAt() + "'" +
            ", reason='" + getReason() + "'" +
            ", booking=" + getBooking() +
            ", professional=" + getProfessional() +
            "}";
    }
}

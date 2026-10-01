package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.SosStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.SosAlert} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SosAlertDTO implements Serializable {

    private Long id;

    private Double latitude;

    private Double longitude;

    @NotNull
    private SosStatus status;

    private Instant createdAt;

    private Instant resolvedAt;

    @NotNull
    private UserDTO raisedBy;

    private UserDTO handledBy;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public SosStatus getStatus() {
        return status;
    }

    public void setStatus(SosStatus status) {
        this.status = status;
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

    public UserDTO getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(UserDTO raisedBy) {
        this.raisedBy = raisedBy;
    }

    public UserDTO getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(UserDTO handledBy) {
        this.handledBy = handledBy;
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
        if (!(o instanceof SosAlertDTO)) {
            return false;
        }

        SosAlertDTO sosAlertDTO = (SosAlertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sosAlertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SosAlertDTO{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", resolvedAt='" + getResolvedAt() + "'" +
            ", raisedBy=" + getRaisedBy() +
            ", handledBy=" + getHandledBy() +
            ", booking=" + getBooking() +
            "}";
    }
}

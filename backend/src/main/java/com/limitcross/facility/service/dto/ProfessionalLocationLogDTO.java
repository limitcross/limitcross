package com.limitcross.facility.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalLocationLog} entity.
 */
@Schema(description = "High volume. Add monthly partitioning in a custom Liquibase changelog. No FKs on purpose.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalLocationLogDTO implements Serializable {

    private Long id;

    @NotNull
    private Long professionalId;

    @Size(max = 36)
    private String bookingRef;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private Double speedKmph;

    @Min(value = 0)
    @Max(value = 100)
    private Integer batteryPct;

    @NotNull
    private Instant recordedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getBookingRef() {
        return bookingRef;
    }

    public void setBookingRef(String bookingRef) {
        this.bookingRef = bookingRef;
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

    public Double getSpeedKmph() {
        return speedKmph;
    }

    public void setSpeedKmph(Double speedKmph) {
        this.speedKmph = speedKmph;
    }

    public Integer getBatteryPct() {
        return batteryPct;
    }

    public void setBatteryPct(Integer batteryPct) {
        this.batteryPct = batteryPct;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalLocationLogDTO)) {
            return false;
        }

        ProfessionalLocationLogDTO professionalLocationLogDTO = (ProfessionalLocationLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalLocationLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalLocationLogDTO{" +
            "id=" + getId() +
            ", professionalId=" + getProfessionalId() +
            ", bookingRef='" + getBookingRef() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", speedKmph=" + getSpeedKmph() +
            ", batteryPct=" + getBatteryPct() +
            ", recordedAt='" + getRecordedAt() + "'" +
            "}";
    }
}

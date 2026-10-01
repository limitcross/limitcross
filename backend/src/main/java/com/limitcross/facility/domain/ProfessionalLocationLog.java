package com.limitcross.facility.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * High volume. Add monthly partitioning in a custom Liquibase changelog. No FKs on purpose.
 */
@Entity
@Table(name = "professional_location_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalLocationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @Size(max = 36)
    @Column(name = "booking_ref", length = 36)
    private String bookingRef;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "speed_kmph")
    private Double speedKmph;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "battery_pct")
    private Integer batteryPct;

    @NotNull
    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalLocationLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfessionalId() {
        return this.professionalId;
    }

    public ProfessionalLocationLog professionalId(Long professionalId) {
        this.setProfessionalId(professionalId);
        return this;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getBookingRef() {
        return this.bookingRef;
    }

    public ProfessionalLocationLog bookingRef(String bookingRef) {
        this.setBookingRef(bookingRef);
        return this;
    }

    public void setBookingRef(String bookingRef) {
        this.bookingRef = bookingRef;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public ProfessionalLocationLog latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public ProfessionalLocationLog longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeedKmph() {
        return this.speedKmph;
    }

    public ProfessionalLocationLog speedKmph(Double speedKmph) {
        this.setSpeedKmph(speedKmph);
        return this;
    }

    public void setSpeedKmph(Double speedKmph) {
        this.speedKmph = speedKmph;
    }

    public Integer getBatteryPct() {
        return this.batteryPct;
    }

    public ProfessionalLocationLog batteryPct(Integer batteryPct) {
        this.setBatteryPct(batteryPct);
        return this;
    }

    public void setBatteryPct(Integer batteryPct) {
        this.batteryPct = batteryPct;
    }

    public Instant getRecordedAt() {
        return this.recordedAt;
    }

    public ProfessionalLocationLog recordedAt(Instant recordedAt) {
        this.setRecordedAt(recordedAt);
        return this;
    }

    public void setRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalLocationLog)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalLocationLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalLocationLog{" +
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

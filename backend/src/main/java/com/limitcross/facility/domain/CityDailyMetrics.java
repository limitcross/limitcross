package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A CityDailyMetrics.
 */
@Entity
@Table(name = "city_daily_metrics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CityDailyMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "metric_date", nullable = false)
    private LocalDate metricDate;

    @Min(value = 0)
    @Column(name = "bookings_created")
    private Integer bookingsCreated;

    @Min(value = 0)
    @Column(name = "bookings_completed")
    private Integer bookingsCompleted;

    @Min(value = 0)
    @Column(name = "bookings_cancelled")
    private Integer bookingsCancelled;

    @Column(name = "gmv", precision = 21, scale = 2)
    private BigDecimal gmv;

    @Column(name = "platform_revenue", precision = 21, scale = 2)
    private BigDecimal platformRevenue;

    @Column(name = "avg_rating")
    private Double avgRating;

    @Column(name = "avg_assignment_sec")
    private Integer avgAssignmentSec;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "services", "parent" }, allowSetters = true)
    private ServiceCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CityDailyMetrics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMetricDate() {
        return this.metricDate;
    }

    public CityDailyMetrics metricDate(LocalDate metricDate) {
        this.setMetricDate(metricDate);
        return this;
    }

    public void setMetricDate(LocalDate metricDate) {
        this.metricDate = metricDate;
    }

    public Integer getBookingsCreated() {
        return this.bookingsCreated;
    }

    public CityDailyMetrics bookingsCreated(Integer bookingsCreated) {
        this.setBookingsCreated(bookingsCreated);
        return this;
    }

    public void setBookingsCreated(Integer bookingsCreated) {
        this.bookingsCreated = bookingsCreated;
    }

    public Integer getBookingsCompleted() {
        return this.bookingsCompleted;
    }

    public CityDailyMetrics bookingsCompleted(Integer bookingsCompleted) {
        this.setBookingsCompleted(bookingsCompleted);
        return this;
    }

    public void setBookingsCompleted(Integer bookingsCompleted) {
        this.bookingsCompleted = bookingsCompleted;
    }

    public Integer getBookingsCancelled() {
        return this.bookingsCancelled;
    }

    public CityDailyMetrics bookingsCancelled(Integer bookingsCancelled) {
        this.setBookingsCancelled(bookingsCancelled);
        return this;
    }

    public void setBookingsCancelled(Integer bookingsCancelled) {
        this.bookingsCancelled = bookingsCancelled;
    }

    public BigDecimal getGmv() {
        return this.gmv;
    }

    public CityDailyMetrics gmv(BigDecimal gmv) {
        this.setGmv(gmv);
        return this;
    }

    public void setGmv(BigDecimal gmv) {
        this.gmv = gmv;
    }

    public BigDecimal getPlatformRevenue() {
        return this.platformRevenue;
    }

    public CityDailyMetrics platformRevenue(BigDecimal platformRevenue) {
        this.setPlatformRevenue(platformRevenue);
        return this;
    }

    public void setPlatformRevenue(BigDecimal platformRevenue) {
        this.platformRevenue = platformRevenue;
    }

    public Double getAvgRating() {
        return this.avgRating;
    }

    public CityDailyMetrics avgRating(Double avgRating) {
        this.setAvgRating(avgRating);
        return this;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getAvgAssignmentSec() {
        return this.avgAssignmentSec;
    }

    public CityDailyMetrics avgAssignmentSec(Integer avgAssignmentSec) {
        this.setAvgAssignmentSec(avgAssignmentSec);
        return this;
    }

    public void setAvgAssignmentSec(Integer avgAssignmentSec) {
        this.avgAssignmentSec = avgAssignmentSec;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public CityDailyMetrics city(City city) {
        this.setCity(city);
        return this;
    }

    public ServiceCategory getCategory() {
        return this.category;
    }

    public void setCategory(ServiceCategory serviceCategory) {
        this.category = serviceCategory;
    }

    public CityDailyMetrics category(ServiceCategory serviceCategory) {
        this.setCategory(serviceCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityDailyMetrics)) {
            return false;
        }
        return getId() != null && getId().equals(((CityDailyMetrics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityDailyMetrics{" +
            "id=" + getId() +
            ", metricDate='" + getMetricDate() + "'" +
            ", bookingsCreated=" + getBookingsCreated() +
            ", bookingsCompleted=" + getBookingsCompleted() +
            ", bookingsCancelled=" + getBookingsCancelled() +
            ", gmv=" + getGmv() +
            ", platformRevenue=" + getPlatformRevenue() +
            ", avgRating=" + getAvgRating() +
            ", avgAssignmentSec=" + getAvgAssignmentSec() +
            "}";
    }
}

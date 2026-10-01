package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CityDailyMetrics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CityDailyMetricsDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate metricDate;

    @Min(value = 0)
    private Integer bookingsCreated;

    @Min(value = 0)
    private Integer bookingsCompleted;

    @Min(value = 0)
    private Integer bookingsCancelled;

    private BigDecimal gmv;

    private BigDecimal platformRevenue;

    private Double avgRating;

    private Integer avgAssignmentSec;

    @NotNull
    private CityDTO city;

    @NotNull
    private ServiceCategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMetricDate() {
        return metricDate;
    }

    public void setMetricDate(LocalDate metricDate) {
        this.metricDate = metricDate;
    }

    public Integer getBookingsCreated() {
        return bookingsCreated;
    }

    public void setBookingsCreated(Integer bookingsCreated) {
        this.bookingsCreated = bookingsCreated;
    }

    public Integer getBookingsCompleted() {
        return bookingsCompleted;
    }

    public void setBookingsCompleted(Integer bookingsCompleted) {
        this.bookingsCompleted = bookingsCompleted;
    }

    public Integer getBookingsCancelled() {
        return bookingsCancelled;
    }

    public void setBookingsCancelled(Integer bookingsCancelled) {
        this.bookingsCancelled = bookingsCancelled;
    }

    public BigDecimal getGmv() {
        return gmv;
    }

    public void setGmv(BigDecimal gmv) {
        this.gmv = gmv;
    }

    public BigDecimal getPlatformRevenue() {
        return platformRevenue;
    }

    public void setPlatformRevenue(BigDecimal platformRevenue) {
        this.platformRevenue = platformRevenue;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getAvgAssignmentSec() {
        return avgAssignmentSec;
    }

    public void setAvgAssignmentSec(Integer avgAssignmentSec) {
        this.avgAssignmentSec = avgAssignmentSec;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public ServiceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ServiceCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityDailyMetricsDTO)) {
            return false;
        }

        CityDailyMetricsDTO cityDailyMetricsDTO = (CityDailyMetricsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cityDailyMetricsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityDailyMetricsDTO{" +
            "id=" + getId() +
            ", metricDate='" + getMetricDate() + "'" +
            ", bookingsCreated=" + getBookingsCreated() +
            ", bookingsCompleted=" + getBookingsCompleted() +
            ", bookingsCancelled=" + getBookingsCancelled() +
            ", gmv=" + getGmv() +
            ", platformRevenue=" + getPlatformRevenue() +
            ", avgRating=" + getAvgRating() +
            ", avgAssignmentSec=" + getAvgAssignmentSec() +
            ", city=" + getCity() +
            ", category=" + getCategory() +
            "}";
    }
}

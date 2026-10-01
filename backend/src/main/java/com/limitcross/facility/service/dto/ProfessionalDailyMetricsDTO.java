package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalDailyMetrics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalDailyMetricsDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate metricDate;

    @Min(value = 0)
    private Integer jobsOffered;

    @Min(value = 0)
    private Integer jobsAccepted;

    @Min(value = 0)
    private Integer jobsCompleted;

    @Min(value = 0)
    private Integer jobsCancelled;

    @Min(value = 0)
    private Integer onlineMinutes;

    private BigDecimal earnings;

    private Double avgRating;

    @NotNull
    private ProfessionalDTO professional;

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

    public Integer getJobsOffered() {
        return jobsOffered;
    }

    public void setJobsOffered(Integer jobsOffered) {
        this.jobsOffered = jobsOffered;
    }

    public Integer getJobsAccepted() {
        return jobsAccepted;
    }

    public void setJobsAccepted(Integer jobsAccepted) {
        this.jobsAccepted = jobsAccepted;
    }

    public Integer getJobsCompleted() {
        return jobsCompleted;
    }

    public void setJobsCompleted(Integer jobsCompleted) {
        this.jobsCompleted = jobsCompleted;
    }

    public Integer getJobsCancelled() {
        return jobsCancelled;
    }

    public void setJobsCancelled(Integer jobsCancelled) {
        this.jobsCancelled = jobsCancelled;
    }

    public Integer getOnlineMinutes() {
        return onlineMinutes;
    }

    public void setOnlineMinutes(Integer onlineMinutes) {
        this.onlineMinutes = onlineMinutes;
    }

    public BigDecimal getEarnings() {
        return earnings;
    }

    public void setEarnings(BigDecimal earnings) {
        this.earnings = earnings;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
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
        if (!(o instanceof ProfessionalDailyMetricsDTO)) {
            return false;
        }

        ProfessionalDailyMetricsDTO professionalDailyMetricsDTO = (ProfessionalDailyMetricsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalDailyMetricsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalDailyMetricsDTO{" +
            "id=" + getId() +
            ", metricDate='" + getMetricDate() + "'" +
            ", jobsOffered=" + getJobsOffered() +
            ", jobsAccepted=" + getJobsAccepted() +
            ", jobsCompleted=" + getJobsCompleted() +
            ", jobsCancelled=" + getJobsCancelled() +
            ", onlineMinutes=" + getOnlineMinutes() +
            ", earnings=" + getEarnings() +
            ", avgRating=" + getAvgRating() +
            ", professional=" + getProfessional() +
            "}";
    }
}

package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A ProfessionalDailyMetrics.
 */
@Entity
@Table(name = "professional_daily_metrics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalDailyMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "metric_date", nullable = false)
    private LocalDate metricDate;

    @Min(value = 0)
    @Column(name = "jobs_offered")
    private Integer jobsOffered;

    @Min(value = 0)
    @Column(name = "jobs_accepted")
    private Integer jobsAccepted;

    @Min(value = 0)
    @Column(name = "jobs_completed")
    private Integer jobsCompleted;

    @Min(value = 0)
    @Column(name = "jobs_cancelled")
    private Integer jobsCancelled;

    @Min(value = 0)
    @Column(name = "online_minutes")
    private Integer onlineMinutes;

    @Column(name = "earnings", precision = 21, scale = 2)
    private BigDecimal earnings;

    @Column(name = "avg_rating")
    private Double avgRating;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalDailyMetrics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMetricDate() {
        return this.metricDate;
    }

    public ProfessionalDailyMetrics metricDate(LocalDate metricDate) {
        this.setMetricDate(metricDate);
        return this;
    }

    public void setMetricDate(LocalDate metricDate) {
        this.metricDate = metricDate;
    }

    public Integer getJobsOffered() {
        return this.jobsOffered;
    }

    public ProfessionalDailyMetrics jobsOffered(Integer jobsOffered) {
        this.setJobsOffered(jobsOffered);
        return this;
    }

    public void setJobsOffered(Integer jobsOffered) {
        this.jobsOffered = jobsOffered;
    }

    public Integer getJobsAccepted() {
        return this.jobsAccepted;
    }

    public ProfessionalDailyMetrics jobsAccepted(Integer jobsAccepted) {
        this.setJobsAccepted(jobsAccepted);
        return this;
    }

    public void setJobsAccepted(Integer jobsAccepted) {
        this.jobsAccepted = jobsAccepted;
    }

    public Integer getJobsCompleted() {
        return this.jobsCompleted;
    }

    public ProfessionalDailyMetrics jobsCompleted(Integer jobsCompleted) {
        this.setJobsCompleted(jobsCompleted);
        return this;
    }

    public void setJobsCompleted(Integer jobsCompleted) {
        this.jobsCompleted = jobsCompleted;
    }

    public Integer getJobsCancelled() {
        return this.jobsCancelled;
    }

    public ProfessionalDailyMetrics jobsCancelled(Integer jobsCancelled) {
        this.setJobsCancelled(jobsCancelled);
        return this;
    }

    public void setJobsCancelled(Integer jobsCancelled) {
        this.jobsCancelled = jobsCancelled;
    }

    public Integer getOnlineMinutes() {
        return this.onlineMinutes;
    }

    public ProfessionalDailyMetrics onlineMinutes(Integer onlineMinutes) {
        this.setOnlineMinutes(onlineMinutes);
        return this;
    }

    public void setOnlineMinutes(Integer onlineMinutes) {
        this.onlineMinutes = onlineMinutes;
    }

    public BigDecimal getEarnings() {
        return this.earnings;
    }

    public ProfessionalDailyMetrics earnings(BigDecimal earnings) {
        this.setEarnings(earnings);
        return this;
    }

    public void setEarnings(BigDecimal earnings) {
        this.earnings = earnings;
    }

    public Double getAvgRating() {
        return this.avgRating;
    }

    public ProfessionalDailyMetrics avgRating(Double avgRating) {
        this.setAvgRating(avgRating);
        return this;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ProfessionalDailyMetrics professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalDailyMetrics)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalDailyMetrics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalDailyMetrics{" +
            "id=" + getId() +
            ", metricDate='" + getMetricDate() + "'" +
            ", jobsOffered=" + getJobsOffered() +
            ", jobsAccepted=" + getJobsAccepted() +
            ", jobsCompleted=" + getJobsCompleted() +
            ", jobsCancelled=" + getJobsCancelled() +
            ", onlineMinutes=" + getOnlineMinutes() +
            ", earnings=" + getEarnings() +
            ", avgRating=" + getAvgRating() +
            "}";
    }
}

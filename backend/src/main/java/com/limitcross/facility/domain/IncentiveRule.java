package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.IncentiveMetric;
import com.limitcross.facility.domain.enumeration.IncentivePeriod;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A IncentiveRule.
 */
@Entity
@Table(name = "incentive_rule")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncentiveRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 120)
    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metric", nullable = false)
    private IncentiveMetric metric;

    @NotNull
    @Column(name = "threshold", precision = 21, scale = 2, nullable = false)
    private BigDecimal threshold;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "reward_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal rewardAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    private IncentivePeriod period;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IncentiveRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public IncentiveRule name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IncentiveMetric getMetric() {
        return this.metric;
    }

    public IncentiveRule metric(IncentiveMetric metric) {
        this.setMetric(metric);
        return this;
    }

    public void setMetric(IncentiveMetric metric) {
        this.metric = metric;
    }

    public BigDecimal getThreshold() {
        return this.threshold;
    }

    public IncentiveRule threshold(BigDecimal threshold) {
        this.setThreshold(threshold);
        return this;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public BigDecimal getRewardAmount() {
        return this.rewardAmount;
    }

    public IncentiveRule rewardAmount(BigDecimal rewardAmount) {
        this.setRewardAmount(rewardAmount);
        return this;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public IncentivePeriod getPeriod() {
        return this.period;
    }

    public IncentiveRule period(IncentivePeriod period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(IncentivePeriod period) {
        this.period = period;
    }

    public Boolean getActive() {
        return this.active;
    }

    public IncentiveRule active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public IncentiveRule city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncentiveRule)) {
            return false;
        }
        return getId() != null && getId().equals(((IncentiveRule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncentiveRule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", metric='" + getMetric() + "'" +
            ", threshold=" + getThreshold() +
            ", rewardAmount=" + getRewardAmount() +
            ", period='" + getPeriod() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}

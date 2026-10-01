package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.IncentiveMetric;
import com.limitcross.facility.domain.enumeration.IncentivePeriod;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.IncentiveRule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncentiveRuleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 120)
    private String name;

    @NotNull
    private IncentiveMetric metric;

    @NotNull
    private BigDecimal threshold;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal rewardAmount;

    @NotNull
    private IncentivePeriod period;

    @NotNull
    private Boolean active;

    private CityDTO city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IncentiveMetric getMetric() {
        return metric;
    }

    public void setMetric(IncentiveMetric metric) {
        this.metric = metric;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public IncentivePeriod getPeriod() {
        return period;
    }

    public void setPeriod(IncentivePeriod period) {
        this.period = period;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncentiveRuleDTO)) {
            return false;
        }

        IncentiveRuleDTO incentiveRuleDTO = (IncentiveRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, incentiveRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncentiveRuleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", metric='" + getMetric() + "'" +
            ", threshold=" + getThreshold() +
            ", rewardAmount=" + getRewardAmount() +
            ", period='" + getPeriod() + "'" +
            ", active='" + getActive() + "'" +
            ", city=" + getCity() +
            "}";
    }
}

package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.PolicyAudience;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CancellationPolicy} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CancellationPolicyDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer hoursBeforeStart;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal feePercent;

    @DecimalMin(value = "0")
    private BigDecimal minFee;

    @NotNull
    private PolicyAudience appliesTo;

    @NotNull
    private Boolean active;

    private ServiceCategoryDTO category;

    private FacilityServiceDTO service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHoursBeforeStart() {
        return hoursBeforeStart;
    }

    public void setHoursBeforeStart(Integer hoursBeforeStart) {
        this.hoursBeforeStart = hoursBeforeStart;
    }

    public BigDecimal getFeePercent() {
        return feePercent;
    }

    public void setFeePercent(BigDecimal feePercent) {
        this.feePercent = feePercent;
    }

    public BigDecimal getMinFee() {
        return minFee;
    }

    public void setMinFee(BigDecimal minFee) {
        this.minFee = minFee;
    }

    public PolicyAudience getAppliesTo() {
        return appliesTo;
    }

    public void setAppliesTo(PolicyAudience appliesTo) {
        this.appliesTo = appliesTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ServiceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ServiceCategoryDTO category) {
        this.category = category;
    }

    public FacilityServiceDTO getService() {
        return service;
    }

    public void setService(FacilityServiceDTO service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CancellationPolicyDTO)) {
            return false;
        }

        CancellationPolicyDTO cancellationPolicyDTO = (CancellationPolicyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cancellationPolicyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CancellationPolicyDTO{" +
            "id=" + getId() +
            ", hoursBeforeStart=" + getHoursBeforeStart() +
            ", feePercent=" + getFeePercent() +
            ", minFee=" + getMinFee() +
            ", appliesTo='" + getAppliesTo() + "'" +
            ", active='" + getActive() + "'" +
            ", category=" + getCategory() +
            ", service=" + getService() +
            "}";
    }
}

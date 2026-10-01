package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CommissionRule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommissionRuleDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal commissionPercent;

    @DecimalMin(value = "0")
    private BigDecimal flatFee;

    @NotNull
    private Instant validFrom;

    private Instant validTo;

    private FacilityServiceDTO service;

    private ServiceCategoryDTO category;

    private CityDTO city;

    private ProfessionalTierDTO tier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(BigDecimal commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public BigDecimal getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(BigDecimal flatFee) {
        this.flatFee = flatFee;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public FacilityServiceDTO getService() {
        return service;
    }

    public void setService(FacilityServiceDTO service) {
        this.service = service;
    }

    public ServiceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ServiceCategoryDTO category) {
        this.category = category;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public ProfessionalTierDTO getTier() {
        return tier;
    }

    public void setTier(ProfessionalTierDTO tier) {
        this.tier = tier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommissionRuleDTO)) {
            return false;
        }

        CommissionRuleDTO commissionRuleDTO = (CommissionRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commissionRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommissionRuleDTO{" +
            "id=" + getId() +
            ", commissionPercent=" + getCommissionPercent() +
            ", flatFee=" + getFlatFee() +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            ", service=" + getService() +
            ", category=" + getCategory() +
            ", city=" + getCity() +
            ", tier=" + getTier() +
            "}";
    }
}

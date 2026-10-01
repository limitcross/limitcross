package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalTier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalTierDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String code;

    @NotNull
    @Size(max = 50)
    private String name;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private Double minRating;

    @Min(value = 0)
    private Integer minJobs;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal commissionPercent;

    private Integer dispatchPriority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public Integer getMinJobs() {
        return minJobs;
    }

    public void setMinJobs(Integer minJobs) {
        this.minJobs = minJobs;
    }

    public BigDecimal getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(BigDecimal commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public Integer getDispatchPriority() {
        return dispatchPriority;
    }

    public void setDispatchPriority(Integer dispatchPriority) {
        this.dispatchPriority = dispatchPriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalTierDTO)) {
            return false;
        }

        ProfessionalTierDTO professionalTierDTO = (ProfessionalTierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalTierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalTierDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", minRating=" + getMinRating() +
            ", minJobs=" + getMinJobs() +
            ", commissionPercent=" + getCommissionPercent() +
            ", dispatchPriority=" + getDispatchPriority() +
            "}";
    }
}

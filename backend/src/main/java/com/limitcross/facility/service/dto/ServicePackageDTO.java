package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ServicePackage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicePackageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 120)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal basePrice;

    @DecimalMin(value = "0")
    private BigDecimal mrp;

    @NotNull
    @Min(value = 1)
    private Integer durationMinutes;

    @NotNull
    private Boolean active;

    private Integer sortOrder;

    @NotNull
    private FacilityServiceDTO service;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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
        if (!(o instanceof ServicePackageDTO)) {
            return false;
        }

        ServicePackageDTO servicePackageDTO = (ServicePackageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicePackageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicePackageDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", basePrice=" + getBasePrice() +
            ", mrp=" + getMrp() +
            ", durationMinutes=" + getDurationMinutes() +
            ", active='" + getActive() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", service=" + getService() +
            "}";
    }
}

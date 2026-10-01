package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ServiceAddon} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceAddonDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 120)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    @Min(value = 0)
    private Integer durationMinutes;

    @NotNull
    private Boolean active;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        if (!(o instanceof ServiceAddonDTO)) {
            return false;
        }

        ServiceAddonDTO serviceAddonDTO = (ServiceAddonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceAddonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceAddonDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", durationMinutes=" + getDurationMinutes() +
            ", active='" + getActive() + "'" +
            ", service=" + getService() +
            "}";
    }
}

package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ServiceZone} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceZoneDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 10)
    private String pincode;

    @NotNull
    private Boolean active;

    @NotNull
    private CityDTO city;

    private Set<ProfessionalDTO> professionals = new HashSet<>();

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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public Set<ProfessionalDTO> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(Set<ProfessionalDTO> professionals) {
        this.professionals = professionals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceZoneDTO)) {
            return false;
        }

        ServiceZoneDTO serviceZoneDTO = (ServiceZoneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceZoneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceZoneDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pincode='" + getPincode() + "'" +
            ", active='" + getActive() + "'" +
            ", city=" + getCity() +
            ", professionals=" + getProfessionals() +
            "}";
    }
}

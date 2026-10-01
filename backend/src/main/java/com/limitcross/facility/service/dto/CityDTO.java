package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.City} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CityDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String name;

    @NotNull
    @Size(max = 80)
    private String stateName;

    @NotNull
    @Size(min = 2, max = 2)
    private String countryCode;

    @NotNull
    @Size(max = 40)
    private String timezone;

    @NotNull
    private Boolean active;

    private Instant createdAt;

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityDTO)) {
            return false;
        }

        CityDTO cityDTO = (CityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", stateName='" + getStateName() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", timezone='" + getTimezone() + "'" +
            ", active='" + getActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

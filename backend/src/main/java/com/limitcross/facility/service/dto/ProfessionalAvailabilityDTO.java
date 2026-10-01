package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalAvailability} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalAvailabilityDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 7)
    private Integer dayOfWeek;

    @NotNull
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$")
    private String startTime;

    @NotNull
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$")
    private String endTime;

    @NotNull
    private ProfessionalDTO professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalAvailabilityDTO)) {
            return false;
        }

        ProfessionalAvailabilityDTO professionalAvailabilityDTO = (ProfessionalAvailabilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalAvailabilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalAvailabilityDTO{" +
            "id=" + getId() +
            ", dayOfWeek=" + getDayOfWeek() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", professional=" + getProfessional() +
            "}";
    }
}

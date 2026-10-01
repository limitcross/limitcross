package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalSkill} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalSkillDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    @Max(value = 5)
    private Integer skillLevel;

    private LocalDate certifiedAt;

    @NotNull
    private FacilityServiceDTO service;

    @NotNull
    private ProfessionalDTO professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }

    public LocalDate getCertifiedAt() {
        return certifiedAt;
    }

    public void setCertifiedAt(LocalDate certifiedAt) {
        this.certifiedAt = certifiedAt;
    }

    public FacilityServiceDTO getService() {
        return service;
    }

    public void setService(FacilityServiceDTO service) {
        this.service = service;
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
        if (!(o instanceof ProfessionalSkillDTO)) {
            return false;
        }

        ProfessionalSkillDTO professionalSkillDTO = (ProfessionalSkillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalSkillDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalSkillDTO{" +
            "id=" + getId() +
            ", skillLevel=" + getSkillLevel() +
            ", certifiedAt='" + getCertifiedAt() + "'" +
            ", service=" + getService() +
            ", professional=" + getProfessional() +
            "}";
    }
}

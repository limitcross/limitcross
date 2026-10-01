package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ProfessionalSkill.
 */
@Entity
@Table(name = "professional_skill")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "skill_level")
    private Integer skillLevel;

    @Column(name = "certified_at")
    private LocalDate certifiedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalSkill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSkillLevel() {
        return this.skillLevel;
    }

    public ProfessionalSkill skillLevel(Integer skillLevel) {
        this.setSkillLevel(skillLevel);
        return this;
    }

    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }

    public LocalDate getCertifiedAt() {
        return this.certifiedAt;
    }

    public ProfessionalSkill certifiedAt(LocalDate certifiedAt) {
        this.setCertifiedAt(certifiedAt);
        return this;
    }

    public void setCertifiedAt(LocalDate certifiedAt) {
        this.certifiedAt = certifiedAt;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public ProfessionalSkill service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ProfessionalSkill professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalSkill)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalSkill) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalSkill{" +
            "id=" + getId() +
            ", skillLevel=" + getSkillLevel() +
            ", certifiedAt='" + getCertifiedAt() + "'" +
            "}";
    }
}

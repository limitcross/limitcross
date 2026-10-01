package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A ProfessionalAvailability.
 */
@Entity
@Table(name = "professional_availability")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalAvailability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 7)
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @NotNull
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$")
    @Column(name = "start_time", nullable = false)
    private String startTime;

    @NotNull
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$")
    @Column(name = "end_time", nullable = false)
    private String endTime;

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

    public ProfessionalAvailability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDayOfWeek() {
        return this.dayOfWeek;
    }

    public ProfessionalAvailability dayOfWeek(Integer dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
        return this;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public ProfessionalAvailability startTime(String startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public ProfessionalAvailability endTime(String endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ProfessionalAvailability professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalAvailability)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalAvailability) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalAvailability{" +
            "id=" + getId() +
            ", dayOfWeek=" + getDayOfWeek() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}

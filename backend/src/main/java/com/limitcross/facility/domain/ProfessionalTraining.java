package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.TrainingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ProfessionalTraining.
 */
@Entity
@Table(name = "professional_training")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalTraining implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "score")
    private Integer score;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TrainingStatus status;

    @Column(name = "completed_at")
    private Instant completedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private TrainingModule module;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalTraining id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return this.score;
    }

    public ProfessionalTraining score(Integer score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public TrainingStatus getStatus() {
        return this.status;
    }

    public ProfessionalTraining status(TrainingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TrainingStatus status) {
        this.status = status;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public ProfessionalTraining completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public ProfessionalTraining professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    public TrainingModule getModule() {
        return this.module;
    }

    public void setModule(TrainingModule trainingModule) {
        this.module = trainingModule;
    }

    public ProfessionalTraining module(TrainingModule trainingModule) {
        this.setModule(trainingModule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalTraining)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalTraining) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalTraining{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", status='" + getStatus() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            "}";
    }
}

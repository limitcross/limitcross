package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.TrainingStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ProfessionalTraining} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalTrainingDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    @Max(value = 100)
    private Integer score;

    @NotNull
    private TrainingStatus status;

    private Instant completedAt;

    @NotNull
    private ProfessionalDTO professional;

    @NotNull
    private TrainingModuleDTO module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public TrainingStatus getStatus() {
        return status;
    }

    public void setStatus(TrainingStatus status) {
        this.status = status;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }

    public TrainingModuleDTO getModule() {
        return module;
    }

    public void setModule(TrainingModuleDTO module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalTrainingDTO)) {
            return false;
        }

        ProfessionalTrainingDTO professionalTrainingDTO = (ProfessionalTrainingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, professionalTrainingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalTrainingDTO{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", status='" + getStatus() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", professional=" + getProfessional() +
            ", module=" + getModule() +
            "}";
    }
}

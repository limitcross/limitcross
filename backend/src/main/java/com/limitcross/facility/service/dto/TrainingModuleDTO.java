package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.TrainingModule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingModuleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String title;

    @Size(max = 255)
    private String contentUrl;

    private Boolean mandatory;

    @Min(value = 0)
    @Max(value = 100)
    private Integer passScore;

    private FacilityServiceDTO service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Integer getPassScore() {
        return passScore;
    }

    public void setPassScore(Integer passScore) {
        this.passScore = passScore;
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
        if (!(o instanceof TrainingModuleDTO)) {
            return false;
        }

        TrainingModuleDTO trainingModuleDTO = (TrainingModuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trainingModuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingModuleDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", contentUrl='" + getContentUrl() + "'" +
            ", mandatory='" + getMandatory() + "'" +
            ", passScore=" + getPassScore() +
            ", service=" + getService() +
            "}";
    }
}

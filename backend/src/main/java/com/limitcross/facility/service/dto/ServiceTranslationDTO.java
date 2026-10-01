package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.ServiceTranslation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceTranslationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String langKey;

    @NotNull
    @Size(max = 120)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    private FacilityServiceDTO service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof ServiceTranslationDTO)) {
            return false;
        }

        ServiceTranslationDTO serviceTranslationDTO = (ServiceTranslationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceTranslationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceTranslationDTO{" +
            "id=" + getId() +
            ", langKey='" + getLangKey() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", service=" + getService() +
            "}";
    }
}

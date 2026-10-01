package com.limitcross.facility.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.AppConfig} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppConfigDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String configKey;

    @Lob
    private String configValue;

    @Size(max = 200)
    private String description;

    @Size(max = 50)
    private String updatedBy;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppConfigDTO)) {
            return false;
        }

        AppConfigDTO appConfigDTO = (AppConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppConfigDTO{" +
            "id=" + getId() +
            ", configKey='" + getConfigKey() + "'" +
            ", configValue='" + getConfigValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

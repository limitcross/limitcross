package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.FeatureFlag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeatureFlagDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 60)
    private String flagKey;

    @NotNull
    private Boolean enabled;

    @Min(value = 0)
    @Max(value = 100)
    private Integer rolloutPercent;

    @Size(max = 500)
    private String cityIds;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlagKey() {
        return flagKey;
    }

    public void setFlagKey(String flagKey) {
        this.flagKey = flagKey;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getRolloutPercent() {
        return rolloutPercent;
    }

    public void setRolloutPercent(Integer rolloutPercent) {
        this.rolloutPercent = rolloutPercent;
    }

    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
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
        if (!(o instanceof FeatureFlagDTO)) {
            return false;
        }

        FeatureFlagDTO featureFlagDTO = (FeatureFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, featureFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeatureFlagDTO{" +
            "id=" + getId() +
            ", flagKey='" + getFlagKey() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", rolloutPercent=" + getRolloutPercent() +
            ", cityIds='" + getCityIds() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

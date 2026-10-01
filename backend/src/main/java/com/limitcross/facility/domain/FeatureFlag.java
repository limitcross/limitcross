package com.limitcross.facility.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A FeatureFlag.
 */
@Entity
@Table(name = "feature_flag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeatureFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "flag_key", length = 60, nullable = false, unique = true)
    private String flagKey;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "rollout_percent")
    private Integer rolloutPercent;

    @Size(max = 500)
    @Column(name = "city_ids", length = 500)
    private String cityIds;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeatureFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlagKey() {
        return this.flagKey;
    }

    public FeatureFlag flagKey(String flagKey) {
        this.setFlagKey(flagKey);
        return this;
    }

    public void setFlagKey(String flagKey) {
        this.flagKey = flagKey;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public FeatureFlag enabled(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getRolloutPercent() {
        return this.rolloutPercent;
    }

    public FeatureFlag rolloutPercent(Integer rolloutPercent) {
        this.setRolloutPercent(rolloutPercent);
        return this;
    }

    public void setRolloutPercent(Integer rolloutPercent) {
        this.rolloutPercent = rolloutPercent;
    }

    public String getCityIds() {
        return this.cityIds;
    }

    public FeatureFlag cityIds(String cityIds) {
        this.setCityIds(cityIds);
        return this;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public FeatureFlag updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeatureFlag)) {
            return false;
        }
        return getId() != null && getId().equals(((FeatureFlag) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeatureFlag{" +
            "id=" + getId() +
            ", flagKey='" + getFlagKey() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", rolloutPercent=" + getRolloutPercent() +
            ", cityIds='" + getCityIds() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

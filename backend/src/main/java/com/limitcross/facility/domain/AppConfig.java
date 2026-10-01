package com.limitcross.facility.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A AppConfig.
 */
@Entity
@Table(name = "app_config")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "config_key", length = 80, nullable = false, unique = true)
    private String configKey;

    @Lob
    @Column(name = "config_value", nullable = false)
    private String configValue;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @Size(max = 50)
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return this.configKey;
    }

    public AppConfig configKey(String configKey) {
        this.setConfigKey(configKey);
        return this;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return this.configValue;
    }

    public AppConfig configValue(String configValue) {
        this.setConfigValue(configValue);
        return this;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return this.description;
    }

    public AppConfig description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public AppConfig updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public AppConfig updatedAt(Instant updatedAt) {
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
        if (!(o instanceof AppConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((AppConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppConfig{" +
            "id=" + getId() +
            ", configKey='" + getConfigKey() + "'" +
            ", configValue='" + getConfigValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}

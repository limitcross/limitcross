package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A ServiceTranslation.
 */
@Entity
@Table(name = "service_translation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceTranslation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "lang_key", length = 10, nullable = false)
    private String langKey;

    @NotNull
    @Size(max = 120)
    @Column(name = "title", length = 120, nullable = false)
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceTranslation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLangKey() {
        return this.langKey;
    }

    public ServiceTranslation langKey(String langKey) {
        this.setLangKey(langKey);
        return this;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getTitle() {
        return this.title;
    }

    public ServiceTranslation title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public ServiceTranslation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public ServiceTranslation service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceTranslation)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceTranslation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceTranslation{" +
            "id=" + getId() +
            ", langKey='" + getLangKey() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}

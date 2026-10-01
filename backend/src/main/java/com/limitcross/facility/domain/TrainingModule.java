package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A TrainingModule.
 */
@Entity
@Table(name = "training_module")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "content_url", length = 255)
    private String contentUrl;

    @Column(name = "mandatory")
    private Boolean mandatory;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "pass_score")
    private Integer passScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrainingModule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public TrainingModule title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return this.contentUrl;
    }

    public TrainingModule contentUrl(String contentUrl) {
        this.setContentUrl(contentUrl);
        return this;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Boolean getMandatory() {
        return this.mandatory;
    }

    public TrainingModule mandatory(Boolean mandatory) {
        this.setMandatory(mandatory);
        return this;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Integer getPassScore() {
        return this.passScore;
    }

    public TrainingModule passScore(Integer passScore) {
        this.setPassScore(passScore);
        return this;
    }

    public void setPassScore(Integer passScore) {
        this.passScore = passScore;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public TrainingModule service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingModule)) {
            return false;
        }
        return getId() != null && getId().equals(((TrainingModule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingModule{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", contentUrl='" + getContentUrl() + "'" +
            ", mandatory='" + getMandatory() + "'" +
            ", passScore=" + getPassScore() +
            "}";
    }
}

package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A SearchKeyword.
 */
@Entity
@Table(name = "search_keyword")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchKeyword implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "keyword", length = 80, nullable = false)
    private String keyword;

    @Column(name = "weight")
    private Integer weight;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchKeyword id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public SearchKeyword keyword(String keyword) {
        this.setKeyword(keyword);
        return this;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public SearchKeyword weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public SearchKeyword service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchKeyword)) {
            return false;
        }
        return getId() != null && getId().equals(((SearchKeyword) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchKeyword{" +
            "id=" + getId() +
            ", keyword='" + getKeyword() + "'" +
            ", weight=" + getWeight() +
            "}";
    }
}

package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ServiceCategory.
 */
@Entity
@Table(name = "service_category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "code", length = 40, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "icon", length = 255)
    private String icon;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private Set<FacilityService> services = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "services", "parent" }, allowSetters = true)
    private ServiceCategory parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ServiceCategory code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public ServiceCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return this.icon;
    }

    public ServiceCategory icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public ServiceCategory sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ServiceCategory active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<FacilityService> getServices() {
        return this.services;
    }

    public void setServices(Set<FacilityService> facilityServices) {
        if (this.services != null) {
            this.services.forEach(i -> i.setCategory(null));
        }
        if (facilityServices != null) {
            facilityServices.forEach(i -> i.setCategory(this));
        }
        this.services = facilityServices;
    }

    public ServiceCategory services(Set<FacilityService> facilityServices) {
        this.setServices(facilityServices);
        return this;
    }

    public ServiceCategory addService(FacilityService facilityService) {
        this.services.add(facilityService);
        facilityService.setCategory(this);
        return this;
    }

    public ServiceCategory removeService(FacilityService facilityService) {
        this.services.remove(facilityService);
        facilityService.setCategory(null);
        return this;
    }

    public ServiceCategory getParent() {
        return this.parent;
    }

    public void setParent(ServiceCategory serviceCategory) {
        this.parent = serviceCategory;
    }

    public ServiceCategory parent(ServiceCategory serviceCategory) {
        this.setParent(serviceCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceCategory{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", active='" + getActive() + "'" +
            "}";
    }
}

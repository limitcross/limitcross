package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ServicePackage.
 */
@Entity
@Table(name = "service_package")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicePackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 120)
    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "base_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @DecimalMin(value = "0")
    @Column(name = "mrp", precision = 21, scale = 2)
    private BigDecimal mrp;

    @NotNull
    @Min(value = 1)
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServicePackage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ServicePackage name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ServicePackage description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public ServicePackage basePrice(BigDecimal basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getMrp() {
        return this.mrp;
    }

    public ServicePackage mrp(BigDecimal mrp) {
        this.setMrp(mrp);
        return this;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
    }

    public Integer getDurationMinutes() {
        return this.durationMinutes;
    }

    public ServicePackage durationMinutes(Integer durationMinutes) {
        this.setDurationMinutes(durationMinutes);
        return this;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Boolean getActive() {
        return this.active;
    }

    public ServicePackage active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public ServicePackage sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public ServicePackage service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicePackage)) {
            return false;
        }
        return getId() != null && getId().equals(((ServicePackage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicePackage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", basePrice=" + getBasePrice() +
            ", mrp=" + getMrp() +
            ", durationMinutes=" + getDurationMinutes() +
            ", active='" + getActive() + "'" +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}

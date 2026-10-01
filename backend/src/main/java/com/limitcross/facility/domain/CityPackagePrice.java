package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A CityPackagePrice.
 */
@Entity
@Table(name = "city_package_price")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CityPackagePrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @DecimalMin(value = "0")
    @Column(name = "surge_multiplier", precision = 21, scale = 2)
    private BigDecimal surgeMultiplier;

    @NotNull
    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private ServicePackage servicePackage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CityPackagePrice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public CityPackagePrice price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSurgeMultiplier() {
        return this.surgeMultiplier;
    }

    public CityPackagePrice surgeMultiplier(BigDecimal surgeMultiplier) {
        this.setSurgeMultiplier(surgeMultiplier);
        return this;
    }

    public void setSurgeMultiplier(BigDecimal surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    public Instant getValidFrom() {
        return this.validFrom;
    }

    public CityPackagePrice validFrom(Instant validFrom) {
        this.setValidFrom(validFrom);
        return this;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return this.validTo;
    }

    public CityPackagePrice validTo(Instant validTo) {
        this.setValidTo(validTo);
        return this;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public ServicePackage getServicePackage() {
        return this.servicePackage;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }

    public CityPackagePrice servicePackage(ServicePackage servicePackage) {
        this.setServicePackage(servicePackage);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public CityPackagePrice city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityPackagePrice)) {
            return false;
        }
        return getId() != null && getId().equals(((CityPackagePrice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityPackagePrice{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", surgeMultiplier=" + getSurgeMultiplier() +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            "}";
    }
}

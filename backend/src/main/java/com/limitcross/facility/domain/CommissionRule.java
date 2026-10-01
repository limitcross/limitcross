package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A CommissionRule.
 */
@Entity
@Table(name = "commission_rule")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommissionRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "commission_percent", precision = 21, scale = 2, nullable = false)
    private BigDecimal commissionPercent;

    @DecimalMin(value = "0")
    @Column(name = "flat_fee", precision = 21, scale = 2)
    private BigDecimal flatFee;

    @NotNull
    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "services", "parent" }, allowSetters = true)
    private ServiceCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProfessionalTier tier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommissionRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCommissionPercent() {
        return this.commissionPercent;
    }

    public CommissionRule commissionPercent(BigDecimal commissionPercent) {
        this.setCommissionPercent(commissionPercent);
        return this;
    }

    public void setCommissionPercent(BigDecimal commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public BigDecimal getFlatFee() {
        return this.flatFee;
    }

    public CommissionRule flatFee(BigDecimal flatFee) {
        this.setFlatFee(flatFee);
        return this;
    }

    public void setFlatFee(BigDecimal flatFee) {
        this.flatFee = flatFee;
    }

    public Instant getValidFrom() {
        return this.validFrom;
    }

    public CommissionRule validFrom(Instant validFrom) {
        this.setValidFrom(validFrom);
        return this;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return this.validTo;
    }

    public CommissionRule validTo(Instant validTo) {
        this.setValidTo(validTo);
        return this;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public CommissionRule service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    public ServiceCategory getCategory() {
        return this.category;
    }

    public void setCategory(ServiceCategory serviceCategory) {
        this.category = serviceCategory;
    }

    public CommissionRule category(ServiceCategory serviceCategory) {
        this.setCategory(serviceCategory);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public CommissionRule city(City city) {
        this.setCity(city);
        return this;
    }

    public ProfessionalTier getTier() {
        return this.tier;
    }

    public void setTier(ProfessionalTier professionalTier) {
        this.tier = professionalTier;
    }

    public CommissionRule tier(ProfessionalTier professionalTier) {
        this.setTier(professionalTier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommissionRule)) {
            return false;
        }
        return getId() != null && getId().equals(((CommissionRule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommissionRule{" +
            "id=" + getId() +
            ", commissionPercent=" + getCommissionPercent() +
            ", flatFee=" + getFlatFee() +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            "}";
    }
}

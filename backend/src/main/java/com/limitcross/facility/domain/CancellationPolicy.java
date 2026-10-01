package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.PolicyAudience;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A CancellationPolicy.
 */
@Entity
@Table(name = "cancellation_policy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CancellationPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "hours_before_start", nullable = false)
    private Integer hoursBeforeStart;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "fee_percent", precision = 21, scale = 2, nullable = false)
    private BigDecimal feePercent;

    @DecimalMin(value = "0")
    @Column(name = "min_fee", precision = 21, scale = 2)
    private BigDecimal minFee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "applies_to", nullable = false)
    private PolicyAudience appliesTo;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "services", "parent" }, allowSetters = true)
    private ServiceCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CancellationPolicy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHoursBeforeStart() {
        return this.hoursBeforeStart;
    }

    public CancellationPolicy hoursBeforeStart(Integer hoursBeforeStart) {
        this.setHoursBeforeStart(hoursBeforeStart);
        return this;
    }

    public void setHoursBeforeStart(Integer hoursBeforeStart) {
        this.hoursBeforeStart = hoursBeforeStart;
    }

    public BigDecimal getFeePercent() {
        return this.feePercent;
    }

    public CancellationPolicy feePercent(BigDecimal feePercent) {
        this.setFeePercent(feePercent);
        return this;
    }

    public void setFeePercent(BigDecimal feePercent) {
        this.feePercent = feePercent;
    }

    public BigDecimal getMinFee() {
        return this.minFee;
    }

    public CancellationPolicy minFee(BigDecimal minFee) {
        this.setMinFee(minFee);
        return this;
    }

    public void setMinFee(BigDecimal minFee) {
        this.minFee = minFee;
    }

    public PolicyAudience getAppliesTo() {
        return this.appliesTo;
    }

    public CancellationPolicy appliesTo(PolicyAudience appliesTo) {
        this.setAppliesTo(appliesTo);
        return this;
    }

    public void setAppliesTo(PolicyAudience appliesTo) {
        this.appliesTo = appliesTo;
    }

    public Boolean getActive() {
        return this.active;
    }

    public CancellationPolicy active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ServiceCategory getCategory() {
        return this.category;
    }

    public void setCategory(ServiceCategory serviceCategory) {
        this.category = serviceCategory;
    }

    public CancellationPolicy category(ServiceCategory serviceCategory) {
        this.setCategory(serviceCategory);
        return this;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public CancellationPolicy service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CancellationPolicy)) {
            return false;
        }
        return getId() != null && getId().equals(((CancellationPolicy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CancellationPolicy{" +
            "id=" + getId() +
            ", hoursBeforeStart=" + getHoursBeforeStart() +
            ", feePercent=" + getFeePercent() +
            ", minFee=" + getMinFee() +
            ", appliesTo='" + getAppliesTo() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}

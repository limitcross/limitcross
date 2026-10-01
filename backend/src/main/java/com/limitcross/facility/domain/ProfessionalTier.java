package com.limitcross.facility.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ProfessionalTier.
 */
@Entity
@Table(name = "professional_tier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfessionalTier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "min_rating")
    private Double minRating;

    @Min(value = 0)
    @Column(name = "min_jobs")
    private Integer minJobs;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "commission_percent", precision = 21, scale = 2, nullable = false)
    private BigDecimal commissionPercent;

    @Column(name = "dispatch_priority")
    private Integer dispatchPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfessionalTier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ProfessionalTier code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public ProfessionalTier name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinRating() {
        return this.minRating;
    }

    public ProfessionalTier minRating(Double minRating) {
        this.setMinRating(minRating);
        return this;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public Integer getMinJobs() {
        return this.minJobs;
    }

    public ProfessionalTier minJobs(Integer minJobs) {
        this.setMinJobs(minJobs);
        return this;
    }

    public void setMinJobs(Integer minJobs) {
        this.minJobs = minJobs;
    }

    public BigDecimal getCommissionPercent() {
        return this.commissionPercent;
    }

    public ProfessionalTier commissionPercent(BigDecimal commissionPercent) {
        this.setCommissionPercent(commissionPercent);
        return this;
    }

    public void setCommissionPercent(BigDecimal commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public Integer getDispatchPriority() {
        return this.dispatchPriority;
    }

    public ProfessionalTier dispatchPriority(Integer dispatchPriority) {
        this.setDispatchPriority(dispatchPriority);
        return this;
    }

    public void setDispatchPriority(Integer dispatchPriority) {
        this.dispatchPriority = dispatchPriority;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfessionalTier)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfessionalTier) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfessionalTier{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", minRating=" + getMinRating() +
            ", minJobs=" + getMinJobs() +
            ", commissionPercent=" + getCommissionPercent() +
            ", dispatchPriority=" + getDispatchPriority() +
            "}";
    }
}

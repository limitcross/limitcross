package com.limitcross.facility.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A MembershipPlan.
 */
@Entity
@Table(name = "membership_plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Min(value = 1)
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    @Column(name = "discount_percent", precision = 21, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "free_visit_charge")
    private Boolean freeVisitCharge;

    @Column(name = "priority_support")
    private Boolean prioritySupport;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MembershipPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MembershipPlan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public MembershipPlan price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationDays() {
        return this.durationDays;
    }

    public MembershipPlan durationDays(Integer durationDays) {
        this.setDurationDays(durationDays);
        return this;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public BigDecimal getDiscountPercent() {
        return this.discountPercent;
    }

    public MembershipPlan discountPercent(BigDecimal discountPercent) {
        this.setDiscountPercent(discountPercent);
        return this;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Boolean getFreeVisitCharge() {
        return this.freeVisitCharge;
    }

    public MembershipPlan freeVisitCharge(Boolean freeVisitCharge) {
        this.setFreeVisitCharge(freeVisitCharge);
        return this;
    }

    public void setFreeVisitCharge(Boolean freeVisitCharge) {
        this.freeVisitCharge = freeVisitCharge;
    }

    public Boolean getPrioritySupport() {
        return this.prioritySupport;
    }

    public MembershipPlan prioritySupport(Boolean prioritySupport) {
        this.setPrioritySupport(prioritySupport);
        return this;
    }

    public void setPrioritySupport(Boolean prioritySupport) {
        this.prioritySupport = prioritySupport;
    }

    public Boolean getActive() {
        return this.active;
    }

    public MembershipPlan active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((MembershipPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipPlan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", durationDays=" + getDurationDays() +
            ", discountPercent=" + getDiscountPercent() +
            ", freeVisitCharge='" + getFreeVisitCharge() + "'" +
            ", prioritySupport='" + getPrioritySupport() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}

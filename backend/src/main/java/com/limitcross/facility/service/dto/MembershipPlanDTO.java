package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.MembershipPlan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MembershipPlanDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    @NotNull
    @Min(value = 1)
    private Integer durationDays;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private BigDecimal discountPercent;

    private Boolean freeVisitCharge;

    private Boolean prioritySupport;

    @NotNull
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Boolean getFreeVisitCharge() {
        return freeVisitCharge;
    }

    public void setFreeVisitCharge(Boolean freeVisitCharge) {
        this.freeVisitCharge = freeVisitCharge;
    }

    public Boolean getPrioritySupport() {
        return prioritySupport;
    }

    public void setPrioritySupport(Boolean prioritySupport) {
        this.prioritySupport = prioritySupport;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipPlanDTO)) {
            return false;
        }

        MembershipPlanDTO membershipPlanDTO = (MembershipPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, membershipPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipPlanDTO{" +
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

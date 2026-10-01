package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.SlotCapacity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SlotCapacityDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant slotStart;

    @NotNull
    private Instant slotEnd;

    @NotNull
    @Min(value = 0)
    private Integer capacity;

    @NotNull
    @Min(value = 0)
    private Integer bookedCount;

    private Boolean blocked;

    @DecimalMin(value = "0")
    private BigDecimal priceMultiplier;

    @NotNull
    private ServiceZoneDTO zone;

    @NotNull
    private ServiceCategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSlotStart() {
        return slotStart;
    }

    public void setSlotStart(Instant slotStart) {
        this.slotStart = slotStart;
    }

    public Instant getSlotEnd() {
        return slotEnd;
    }

    public void setSlotEnd(Instant slotEnd) {
        this.slotEnd = slotEnd;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(Integer bookedCount) {
        this.bookedCount = bookedCount;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public BigDecimal getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(BigDecimal priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public ServiceZoneDTO getZone() {
        return zone;
    }

    public void setZone(ServiceZoneDTO zone) {
        this.zone = zone;
    }

    public ServiceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ServiceCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlotCapacityDTO)) {
            return false;
        }

        SlotCapacityDTO slotCapacityDTO = (SlotCapacityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, slotCapacityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotCapacityDTO{" +
            "id=" + getId() +
            ", slotStart='" + getSlotStart() + "'" +
            ", slotEnd='" + getSlotEnd() + "'" +
            ", capacity=" + getCapacity() +
            ", bookedCount=" + getBookedCount() +
            ", blocked='" + getBlocked() + "'" +
            ", priceMultiplier=" + getPriceMultiplier() +
            ", zone=" + getZone() +
            ", category=" + getCategory() +
            "}";
    }
}

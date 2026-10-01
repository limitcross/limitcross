package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A SlotCapacity.
 */
@Entity
@Table(name = "slot_capacity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SlotCapacity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "slot_start", nullable = false)
    private Instant slotStart;

    @NotNull
    @Column(name = "slot_end", nullable = false)
    private Instant slotEnd;

    @NotNull
    @Min(value = 0)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull
    @Min(value = 0)
    @Column(name = "booked_count", nullable = false)
    private Integer bookedCount;

    @Column(name = "blocked")
    private Boolean blocked;

    @DecimalMin(value = "0")
    @Column(name = "price_multiplier", precision = 21, scale = 2)
    private BigDecimal priceMultiplier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "city", "professionals" }, allowSetters = true)
    private ServiceZone zone;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "services", "parent" }, allowSetters = true)
    private ServiceCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SlotCapacity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSlotStart() {
        return this.slotStart;
    }

    public SlotCapacity slotStart(Instant slotStart) {
        this.setSlotStart(slotStart);
        return this;
    }

    public void setSlotStart(Instant slotStart) {
        this.slotStart = slotStart;
    }

    public Instant getSlotEnd() {
        return this.slotEnd;
    }

    public SlotCapacity slotEnd(Instant slotEnd) {
        this.setSlotEnd(slotEnd);
        return this;
    }

    public void setSlotEnd(Instant slotEnd) {
        this.slotEnd = slotEnd;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public SlotCapacity capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getBookedCount() {
        return this.bookedCount;
    }

    public SlotCapacity bookedCount(Integer bookedCount) {
        this.setBookedCount(bookedCount);
        return this;
    }

    public void setBookedCount(Integer bookedCount) {
        this.bookedCount = bookedCount;
    }

    public Boolean getBlocked() {
        return this.blocked;
    }

    public SlotCapacity blocked(Boolean blocked) {
        this.setBlocked(blocked);
        return this;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public BigDecimal getPriceMultiplier() {
        return this.priceMultiplier;
    }

    public SlotCapacity priceMultiplier(BigDecimal priceMultiplier) {
        this.setPriceMultiplier(priceMultiplier);
        return this;
    }

    public void setPriceMultiplier(BigDecimal priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public ServiceZone getZone() {
        return this.zone;
    }

    public void setZone(ServiceZone serviceZone) {
        this.zone = serviceZone;
    }

    public SlotCapacity zone(ServiceZone serviceZone) {
        this.setZone(serviceZone);
        return this;
    }

    public ServiceCategory getCategory() {
        return this.category;
    }

    public void setCategory(ServiceCategory serviceCategory) {
        this.category = serviceCategory;
    }

    public SlotCapacity category(ServiceCategory serviceCategory) {
        this.setCategory(serviceCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlotCapacity)) {
            return false;
        }
        return getId() != null && getId().equals(((SlotCapacity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotCapacity{" +
            "id=" + getId() +
            ", slotStart='" + getSlotStart() + "'" +
            ", slotEnd='" + getSlotEnd() + "'" +
            ", capacity=" + getCapacity() +
            ", bookedCount=" + getBookedCount() +
            ", blocked='" + getBlocked() + "'" +
            ", priceMultiplier=" + getPriceMultiplier() +
            "}";
    }
}

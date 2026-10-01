package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A BookingItem.
 */
@Entity
@Table(name = "booking_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 160)
    @Column(name = "name", length = 160, nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "unit_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "line_total", precision = 21, scale = 2, nullable = false)
    private BigDecimal lineTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private ServicePackage servicePackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private ServiceAddon addon;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "items",
            "statusHistories",
            "assignments",
            "media",
            "payments",
            "quotes",
            "reschedules",
            "customer",
            "service",
            "city",
            "address",
            "professional",
            "coupon",
            "subscription",
            "slotCapacity",
            "couponRedemption",
            "review",
            "chatThread",
        },
        allowSetters = true
    )
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BookingItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BookingItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public BookingItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BookingItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return this.lineTotal;
    }

    public BookingItem lineTotal(BigDecimal lineTotal) {
        this.setLineTotal(lineTotal);
        return this;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public ServicePackage getServicePackage() {
        return this.servicePackage;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }

    public BookingItem servicePackage(ServicePackage servicePackage) {
        this.setServicePackage(servicePackage);
        return this;
    }

    public ServiceAddon getAddon() {
        return this.addon;
    }

    public void setAddon(ServiceAddon serviceAddon) {
        this.addon = serviceAddon;
    }

    public BookingItem addon(ServiceAddon serviceAddon) {
        this.setAddon(serviceAddon);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BookingItem booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingItem)) {
            return false;
        }
        return getId() != null && getId().equals(((BookingItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", quantity=" + getQuantity() +
            ", lineTotal=" + getLineTotal() +
            "}";
    }
}

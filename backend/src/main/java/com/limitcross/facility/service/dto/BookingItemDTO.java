package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BookingItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 160)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitPrice;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal lineTotal;

    private ServicePackageDTO servicePackage;

    private ServiceAddonDTO addon;

    @NotNull
    private BookingDTO booking;

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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public ServicePackageDTO getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackageDTO servicePackage) {
        this.servicePackage = servicePackage;
    }

    public ServiceAddonDTO getAddon() {
        return addon;
    }

    public void setAddon(ServiceAddonDTO addon) {
        this.addon = addon;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingItemDTO)) {
            return false;
        }

        BookingItemDTO bookingItemDTO = (BookingItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", quantity=" + getQuantity() +
            ", lineTotal=" + getLineTotal() +
            ", servicePackage=" + getServicePackage() +
            ", addon=" + getAddon() +
            ", booking=" + getBooking() +
            "}";
    }
}

package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CityPackagePrice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CityPackagePriceDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    @DecimalMin(value = "0")
    private BigDecimal surgeMultiplier;

    @NotNull
    private Instant validFrom;

    private Instant validTo;

    @NotNull
    private ServicePackageDTO servicePackage;

    @NotNull
    private CityDTO city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(BigDecimal surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public ServicePackageDTO getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackageDTO servicePackage) {
        this.servicePackage = servicePackage;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityPackagePriceDTO)) {
            return false;
        }

        CityPackagePriceDTO cityPackagePriceDTO = (CityPackagePriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cityPackagePriceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityPackagePriceDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", surgeMultiplier=" + getSurgeMultiplier() +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            ", servicePackage=" + getServicePackage() +
            ", city=" + getCity() +
            "}";
    }
}

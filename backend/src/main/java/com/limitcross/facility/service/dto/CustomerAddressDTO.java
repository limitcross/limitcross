package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.AddressLabel;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.CustomerAddress} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerAddressDTO implements Serializable {

    private Long id;

    @NotNull
    private AddressLabel label;

    @NotNull
    @Size(max = 100)
    private String contactName;

    @NotNull
    @Size(max = 30)
    private String contactPhone;

    @NotNull
    @Size(max = 200)
    private String line1;

    @Size(max = 200)
    private String line2;

    @Size(max = 200)
    private String landmark;

    @NotNull
    @Size(max = 10)
    private String pincode;

    private Double latitude;

    private Double longitude;

    @NotNull
    private Boolean defaultAddress;

    private Instant deletedAt;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private UserDTO customer;

    @NotNull
    private CityDTO city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressLabel getLabel() {
        return label;
    }

    public void setLabel(AddressLabel label) {
        this.label = label;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserDTO customer) {
        this.customer = customer;
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
        if (!(o instanceof CustomerAddressDTO)) {
            return false;
        }

        CustomerAddressDTO customerAddressDTO = (CustomerAddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerAddressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerAddressDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", contactPhone='" + getContactPhone() + "'" +
            ", line1='" + getLine1() + "'" +
            ", line2='" + getLine2() + "'" +
            ", landmark='" + getLandmark() + "'" +
            ", pincode='" + getPincode() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", defaultAddress='" + getDefaultAddress() + "'" +
            ", deletedAt='" + getDeletedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", customer=" + getCustomer() +
            ", city=" + getCity() +
            "}";
    }
}

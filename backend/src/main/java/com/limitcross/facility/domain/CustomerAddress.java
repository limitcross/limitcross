package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.AddressLabel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A CustomerAddress.
 */
@Entity
@Table(name = "customer_address")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false)
    private AddressLabel label;

    @NotNull
    @Size(max = 100)
    @Column(name = "contact_name", length = 100, nullable = false)
    private String contactName;

    @NotNull
    @Size(max = 30)
    @Column(name = "contact_phone", length = 30, nullable = false)
    private String contactPhone;

    @NotNull
    @Size(max = 200)
    @Column(name = "line_1", length = 200, nullable = false)
    private String line1;

    @Size(max = 200)
    @Column(name = "line_2", length = 200)
    private String line2;

    @Size(max = 200)
    @Column(name = "landmark", length = 200)
    private String landmark;

    @NotNull
    @Size(max = 10)
    @Column(name = "pincode", length = 10, nullable = false)
    private String pincode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @NotNull
    @Column(name = "default_address", nullable = false)
    private Boolean defaultAddress;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User customer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerAddress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressLabel getLabel() {
        return this.label;
    }

    public CustomerAddress label(AddressLabel label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(AddressLabel label) {
        this.label = label;
    }

    public String getContactName() {
        return this.contactName;
    }

    public CustomerAddress contactName(String contactName) {
        this.setContactName(contactName);
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public CustomerAddress contactPhone(String contactPhone) {
        this.setContactPhone(contactPhone);
        return this;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLine1() {
        return this.line1;
    }

    public CustomerAddress line1(String line1) {
        this.setLine1(line1);
        return this;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public CustomerAddress line2(String line2) {
        this.setLine2(line2);
        return this;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLandmark() {
        return this.landmark;
    }

    public CustomerAddress landmark(String landmark) {
        this.setLandmark(landmark);
        return this;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return this.pincode;
    }

    public CustomerAddress pincode(String pincode) {
        this.setPincode(pincode);
        return this;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public CustomerAddress latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public CustomerAddress longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getDefaultAddress() {
        return this.defaultAddress;
    }

    public CustomerAddress defaultAddress(Boolean defaultAddress) {
        this.setDefaultAddress(defaultAddress);
        return this;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public CustomerAddress deletedAt(Instant deletedAt) {
        this.setDeletedAt(deletedAt);
        return this;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CustomerAddress createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public CustomerAddress updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getCustomer() {
        return this.customer;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public CustomerAddress customer(User user) {
        this.setCustomer(user);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public CustomerAddress city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerAddress)) {
            return false;
        }
        return getId() != null && getId().equals(((CustomerAddress) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerAddress{" +
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
            "}";
    }
}

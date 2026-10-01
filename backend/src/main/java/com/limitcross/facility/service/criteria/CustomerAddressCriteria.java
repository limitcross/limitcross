package com.limitcross.facility.service.criteria;

import com.limitcross.facility.domain.enumeration.AddressLabel;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.limitcross.facility.domain.CustomerAddress} entity. This class is used
 * in {@link com.limitcross.facility.web.rest.CustomerAddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customer-addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerAddressCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AddressLabel
     */
    public static class AddressLabelFilter extends Filter<AddressLabel> {

        public AddressLabelFilter() {}

        public AddressLabelFilter(AddressLabelFilter filter) {
            super(filter);
        }

        @Override
        public AddressLabelFilter copy() {
            return new AddressLabelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AddressLabelFilter label;

    private StringFilter contactName;

    private StringFilter contactPhone;

    private StringFilter line1;

    private StringFilter line2;

    private StringFilter landmark;

    private StringFilter pincode;

    private DoubleFilter latitude;

    private DoubleFilter longitude;

    private BooleanFilter defaultAddress;

    private InstantFilter deletedAt;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter customerId;

    private LongFilter cityId;

    private Boolean distinct;

    public CustomerAddressCriteria() {}

    public CustomerAddressCriteria(CustomerAddressCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.label = other.optionalLabel().map(AddressLabelFilter::copy).orElse(null);
        this.contactName = other.optionalContactName().map(StringFilter::copy).orElse(null);
        this.contactPhone = other.optionalContactPhone().map(StringFilter::copy).orElse(null);
        this.line1 = other.optionalLine1().map(StringFilter::copy).orElse(null);
        this.line2 = other.optionalLine2().map(StringFilter::copy).orElse(null);
        this.landmark = other.optionalLandmark().map(StringFilter::copy).orElse(null);
        this.pincode = other.optionalPincode().map(StringFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(DoubleFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(DoubleFilter::copy).orElse(null);
        this.defaultAddress = other.optionalDefaultAddress().map(BooleanFilter::copy).orElse(null);
        this.deletedAt = other.optionalDeletedAt().map(InstantFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.cityId = other.optionalCityId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CustomerAddressCriteria copy() {
        return new CustomerAddressCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public AddressLabelFilter getLabel() {
        return label;
    }

    public Optional<AddressLabelFilter> optionalLabel() {
        return Optional.ofNullable(label);
    }

    public AddressLabelFilter label() {
        if (label == null) {
            setLabel(new AddressLabelFilter());
        }
        return label;
    }

    public void setLabel(AddressLabelFilter label) {
        this.label = label;
    }

    public StringFilter getContactName() {
        return contactName;
    }

    public Optional<StringFilter> optionalContactName() {
        return Optional.ofNullable(contactName);
    }

    public StringFilter contactName() {
        if (contactName == null) {
            setContactName(new StringFilter());
        }
        return contactName;
    }

    public void setContactName(StringFilter contactName) {
        this.contactName = contactName;
    }

    public StringFilter getContactPhone() {
        return contactPhone;
    }

    public Optional<StringFilter> optionalContactPhone() {
        return Optional.ofNullable(contactPhone);
    }

    public StringFilter contactPhone() {
        if (contactPhone == null) {
            setContactPhone(new StringFilter());
        }
        return contactPhone;
    }

    public void setContactPhone(StringFilter contactPhone) {
        this.contactPhone = contactPhone;
    }

    public StringFilter getLine1() {
        return line1;
    }

    public Optional<StringFilter> optionalLine1() {
        return Optional.ofNullable(line1);
    }

    public StringFilter line1() {
        if (line1 == null) {
            setLine1(new StringFilter());
        }
        return line1;
    }

    public void setLine1(StringFilter line1) {
        this.line1 = line1;
    }

    public StringFilter getLine2() {
        return line2;
    }

    public Optional<StringFilter> optionalLine2() {
        return Optional.ofNullable(line2);
    }

    public StringFilter line2() {
        if (line2 == null) {
            setLine2(new StringFilter());
        }
        return line2;
    }

    public void setLine2(StringFilter line2) {
        this.line2 = line2;
    }

    public StringFilter getLandmark() {
        return landmark;
    }

    public Optional<StringFilter> optionalLandmark() {
        return Optional.ofNullable(landmark);
    }

    public StringFilter landmark() {
        if (landmark == null) {
            setLandmark(new StringFilter());
        }
        return landmark;
    }

    public void setLandmark(StringFilter landmark) {
        this.landmark = landmark;
    }

    public StringFilter getPincode() {
        return pincode;
    }

    public Optional<StringFilter> optionalPincode() {
        return Optional.ofNullable(pincode);
    }

    public StringFilter pincode() {
        if (pincode == null) {
            setPincode(new StringFilter());
        }
        return pincode;
    }

    public void setPincode(StringFilter pincode) {
        this.pincode = pincode;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public Optional<DoubleFilter> optionalLatitude() {
        return Optional.ofNullable(latitude);
    }

    public DoubleFilter latitude() {
        if (latitude == null) {
            setLatitude(new DoubleFilter());
        }
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public Optional<DoubleFilter> optionalLongitude() {
        return Optional.ofNullable(longitude);
    }

    public DoubleFilter longitude() {
        if (longitude == null) {
            setLongitude(new DoubleFilter());
        }
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public BooleanFilter getDefaultAddress() {
        return defaultAddress;
    }

    public Optional<BooleanFilter> optionalDefaultAddress() {
        return Optional.ofNullable(defaultAddress);
    }

    public BooleanFilter defaultAddress() {
        if (defaultAddress == null) {
            setDefaultAddress(new BooleanFilter());
        }
        return defaultAddress;
    }

    public void setDefaultAddress(BooleanFilter defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public InstantFilter getDeletedAt() {
        return deletedAt;
    }

    public Optional<InstantFilter> optionalDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    public InstantFilter deletedAt() {
        if (deletedAt == null) {
            setDeletedAt(new InstantFilter());
        }
        return deletedAt;
    }

    public void setDeletedAt(InstantFilter deletedAt) {
        this.deletedAt = deletedAt;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public Optional<LongFilter> optionalCityId() {
        return Optional.ofNullable(cityId);
    }

    public LongFilter cityId() {
        if (cityId == null) {
            setCityId(new LongFilter());
        }
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerAddressCriteria that = (CustomerAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(label, that.label) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(contactPhone, that.contactPhone) &&
            Objects.equals(line1, that.line1) &&
            Objects.equals(line2, that.line2) &&
            Objects.equals(landmark, that.landmark) &&
            Objects.equals(pincode, that.pincode) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(defaultAddress, that.defaultAddress) &&
            Objects.equals(deletedAt, that.deletedAt) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            label,
            contactName,
            contactPhone,
            line1,
            line2,
            landmark,
            pincode,
            latitude,
            longitude,
            defaultAddress,
            deletedAt,
            createdAt,
            updatedAt,
            customerId,
            cityId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerAddressCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLabel().map(f -> "label=" + f + ", ").orElse("") +
            optionalContactName().map(f -> "contactName=" + f + ", ").orElse("") +
            optionalContactPhone().map(f -> "contactPhone=" + f + ", ").orElse("") +
            optionalLine1().map(f -> "line1=" + f + ", ").orElse("") +
            optionalLine2().map(f -> "line2=" + f + ", ").orElse("") +
            optionalLandmark().map(f -> "landmark=" + f + ", ").orElse("") +
            optionalPincode().map(f -> "pincode=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalDefaultAddress().map(f -> "defaultAddress=" + f + ", ").orElse("") +
            optionalDeletedAt().map(f -> "deletedAt=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalCityId().map(f -> "cityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

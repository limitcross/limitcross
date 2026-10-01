package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.SubscriptionFrequency;
import com.limitcross.facility.domain.enumeration.SubscriptionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BookingSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private SubscriptionFrequency frequency;

    @Size(max = 30)
    private String daysOfWeek;

    @NotNull
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$")
    private String preferredTime;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private LocalDate nextRunDate;

    @NotNull
    private SubscriptionStatus status;

    private Boolean autoPay;

    private Instant createdAt;

    @NotNull
    private UserDTO customer;

    @NotNull
    private FacilityServiceDTO service;

    @NotNull
    private ServicePackageDTO servicePackage;

    @NotNull
    private CustomerAddressDTO address;

    private ProfessionalDTO preferredProfessional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(SubscriptionFrequency frequency) {
        this.frequency = frequency;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getNextRunDate() {
        return nextRunDate;
    }

    public void setNextRunDate(LocalDate nextRunDate) {
        this.nextRunDate = nextRunDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public Boolean getAutoPay() {
        return autoPay;
    }

    public void setAutoPay(Boolean autoPay) {
        this.autoPay = autoPay;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserDTO customer) {
        this.customer = customer;
    }

    public FacilityServiceDTO getService() {
        return service;
    }

    public void setService(FacilityServiceDTO service) {
        this.service = service;
    }

    public ServicePackageDTO getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackageDTO servicePackage) {
        this.servicePackage = servicePackage;
    }

    public CustomerAddressDTO getAddress() {
        return address;
    }

    public void setAddress(CustomerAddressDTO address) {
        this.address = address;
    }

    public ProfessionalDTO getPreferredProfessional() {
        return preferredProfessional;
    }

    public void setPreferredProfessional(ProfessionalDTO preferredProfessional) {
        this.preferredProfessional = preferredProfessional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingSubscriptionDTO)) {
            return false;
        }

        BookingSubscriptionDTO bookingSubscriptionDTO = (BookingSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingSubscriptionDTO{" +
            "id=" + getId() +
            ", frequency='" + getFrequency() + "'" +
            ", daysOfWeek='" + getDaysOfWeek() + "'" +
            ", preferredTime='" + getPreferredTime() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", nextRunDate='" + getNextRunDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", autoPay='" + getAutoPay() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", customer=" + getCustomer() +
            ", service=" + getService() +
            ", servicePackage=" + getServicePackage() +
            ", address=" + getAddress() +
            ", preferredProfessional=" + getPreferredProfessional() +
            "}";
    }
}

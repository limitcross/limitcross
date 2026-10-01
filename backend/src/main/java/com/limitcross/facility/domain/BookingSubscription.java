package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.SubscriptionFrequency;
import com.limitcross.facility.domain.enumeration.SubscriptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A BookingSubscription.
 */
@Entity
@Table(name = "booking_subscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private SubscriptionFrequency frequency;

    @Size(max = 30)
    @Column(name = "days_of_week", length = 30)
    private String daysOfWeek;

    @NotNull
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$")
    @Column(name = "preferred_time", nullable = false)
    private String preferredTime;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "next_run_date", nullable = false)
    private LocalDate nextRunDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @Column(name = "auto_pay")
    private Boolean autoPay;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private User customer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicePackages", "addons", "translations", "category" }, allowSetters = true)
    private FacilityService service;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private ServicePackage servicePackage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "customer", "city" }, allowSetters = true)
    private CustomerAddress address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional preferredProfessional;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BookingSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscriptionFrequency getFrequency() {
        return this.frequency;
    }

    public BookingSubscription frequency(SubscriptionFrequency frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(SubscriptionFrequency frequency) {
        this.frequency = frequency;
    }

    public String getDaysOfWeek() {
        return this.daysOfWeek;
    }

    public BookingSubscription daysOfWeek(String daysOfWeek) {
        this.setDaysOfWeek(daysOfWeek);
        return this;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getPreferredTime() {
        return this.preferredTime;
    }

    public BookingSubscription preferredTime(String preferredTime) {
        this.setPreferredTime(preferredTime);
        return this;
    }

    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public BookingSubscription startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public BookingSubscription endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getNextRunDate() {
        return this.nextRunDate;
    }

    public BookingSubscription nextRunDate(LocalDate nextRunDate) {
        this.setNextRunDate(nextRunDate);
        return this;
    }

    public void setNextRunDate(LocalDate nextRunDate) {
        this.nextRunDate = nextRunDate;
    }

    public SubscriptionStatus getStatus() {
        return this.status;
    }

    public BookingSubscription status(SubscriptionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public Boolean getAutoPay() {
        return this.autoPay;
    }

    public BookingSubscription autoPay(Boolean autoPay) {
        this.setAutoPay(autoPay);
        return this;
    }

    public void setAutoPay(Boolean autoPay) {
        this.autoPay = autoPay;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BookingSubscription createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getCustomer() {
        return this.customer;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public BookingSubscription customer(User user) {
        this.setCustomer(user);
        return this;
    }

    public FacilityService getService() {
        return this.service;
    }

    public void setService(FacilityService facilityService) {
        this.service = facilityService;
    }

    public BookingSubscription service(FacilityService facilityService) {
        this.setService(facilityService);
        return this;
    }

    public ServicePackage getServicePackage() {
        return this.servicePackage;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }

    public BookingSubscription servicePackage(ServicePackage servicePackage) {
        this.setServicePackage(servicePackage);
        return this;
    }

    public CustomerAddress getAddress() {
        return this.address;
    }

    public void setAddress(CustomerAddress customerAddress) {
        this.address = customerAddress;
    }

    public BookingSubscription address(CustomerAddress customerAddress) {
        this.setAddress(customerAddress);
        return this;
    }

    public Professional getPreferredProfessional() {
        return this.preferredProfessional;
    }

    public void setPreferredProfessional(Professional professional) {
        this.preferredProfessional = professional;
    }

    public BookingSubscription preferredProfessional(Professional professional) {
        this.setPreferredProfessional(professional);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((BookingSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingSubscription{" +
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
            "}";
    }
}

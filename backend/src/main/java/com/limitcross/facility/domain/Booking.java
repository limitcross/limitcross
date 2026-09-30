package com.limitcross.facility.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @Column(length = 36)
    private String id;

    @NotBlank
    @Column(name = "owner_login", nullable = false, length = 100)
    private String ownerLogin;

    @NotBlank
    @Column(name = "service_id", nullable = false, length = 50)
    private String serviceId;

    @NotBlank
    @Column(name = "service_title", nullable = false, length = 120)
    private String serviceTitle;

    @Column(nullable = false, length = 10)
    private String emoji;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "service_fee", nullable = false)
    private Integer serviceFee = 49;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "time_slot", nullable = false, length = 60)
    private String timeSlot;

    @Column(nullable = false, length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.REQUESTED;

    @Column(name = "assigned_pro_name", nullable = false, length = 120)
    private String assignedProName = "Not assigned yet";

    @Column(name = "pro_rating", nullable = false)
    private Double proRating = 0D;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void initialize() {
        if (id == null) id = UUID.randomUUID().toString();
        if (updatedAt == null) updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getOwnerLogin() { return ownerLogin; }
    public void setOwnerLogin(String ownerLogin) { this.ownerLogin = ownerLogin; }
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public String getServiceTitle() { return serviceTitle; }
    public void setServiceTitle(String serviceTitle) { this.serviceTitle = serviceTitle; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Integer getServiceFee() { return serviceFee; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public String getAssignedProName() { return assignedProName; }
    public Double getProRating() { return proRating; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void touch() { updatedAt = Instant.now(); }
    public Integer getTotalPrice() { return price + serviceFee; }
}
package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.LoyaltyReason;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.LoyaltyLedger} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoyaltyLedgerDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer points;

    @NotNull
    private LoyaltyReason reason;

    private LocalDate expiresAt;

    private Instant createdAt;

    @NotNull
    private UserDTO user;

    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LoyaltyReason getReason() {
        return reason;
    }

    public void setReason(LoyaltyReason reason) {
        this.reason = reason;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
        if (!(o instanceof LoyaltyLedgerDTO)) {
            return false;
        }

        LoyaltyLedgerDTO loyaltyLedgerDTO = (LoyaltyLedgerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loyaltyLedgerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoyaltyLedgerDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", reason='" + getReason() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", user=" + getUser() +
            ", booking=" + getBooking() +
            "}";
    }
}

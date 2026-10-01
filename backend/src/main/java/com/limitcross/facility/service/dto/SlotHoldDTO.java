package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.HoldStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.SlotHold} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SlotHoldDTO implements Serializable {

    private Long id;

    @NotNull
    private HoldStatus status;

    @NotNull
    private Instant expiresAt;

    private Instant createdAt;

    @NotNull
    private UserDTO user;

    private SlotCapacityDTO slotCapacity;

    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HoldStatus getStatus() {
        return status;
    }

    public void setStatus(HoldStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
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

    public SlotCapacityDTO getSlotCapacity() {
        return slotCapacity;
    }

    public void setSlotCapacity(SlotCapacityDTO slotCapacity) {
        this.slotCapacity = slotCapacity;
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
        if (!(o instanceof SlotHoldDTO)) {
            return false;
        }

        SlotHoldDTO slotHoldDTO = (SlotHoldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, slotHoldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotHoldDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", user=" + getUser() +
            ", slotCapacity=" + getSlotCapacity() +
            ", booking=" + getBooking() +
            "}";
    }
}

package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.QuoteStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BookingQuote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingQuoteDTO implements Serializable {

    private Long id;

    @NotNull
    private QuoteStatus status;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalAmount;

    @Size(max = 1000)
    private String notes;

    private Instant validUntil;

    private Instant respondedAt;

    private Instant createdAt;

    @NotNull
    private ProfessionalDTO professional;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuoteStatus getStatus() {
        return status;
    }

    public void setStatus(QuoteStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public Instant getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(Instant respondedAt) {
        this.respondedAt = respondedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ProfessionalDTO getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
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
        if (!(o instanceof BookingQuoteDTO)) {
            return false;
        }

        BookingQuoteDTO bookingQuoteDTO = (BookingQuoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingQuoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingQuoteDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", notes='" + getNotes() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", respondedAt='" + getRespondedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", professional=" + getProfessional() +
            ", booking=" + getBooking() +
            "}";
    }
}

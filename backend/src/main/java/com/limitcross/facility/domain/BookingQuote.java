package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.QuoteStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A BookingQuote.
 */
@Entity
@Table(name = "booking_quote")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingQuote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuoteStatus status;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Size(max = 1000)
    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "valid_until")
    private Instant validUntil;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quote")
    @JsonIgnoreProperties(value = { "quote" }, allowSetters = true)
    private Set<BookingQuoteItem> items = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "kycDocuments", "skills", "availabilities", "timeOffs", "homeCity", "tier", "zones", "professionalWallet" },
        allowSetters = true
    )
    private Professional professional;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "items",
            "statusHistories",
            "assignments",
            "media",
            "payments",
            "quotes",
            "reschedules",
            "customer",
            "service",
            "city",
            "address",
            "professional",
            "coupon",
            "subscription",
            "slotCapacity",
            "couponRedemption",
            "review",
            "chatThread",
        },
        allowSetters = true
    )
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BookingQuote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuoteStatus getStatus() {
        return this.status;
    }

    public BookingQuote status(QuoteStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(QuoteStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public BookingQuote totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotes() {
        return this.notes;
    }

    public BookingQuote notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getValidUntil() {
        return this.validUntil;
    }

    public BookingQuote validUntil(Instant validUntil) {
        this.setValidUntil(validUntil);
        return this;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public Instant getRespondedAt() {
        return this.respondedAt;
    }

    public BookingQuote respondedAt(Instant respondedAt) {
        this.setRespondedAt(respondedAt);
        return this;
    }

    public void setRespondedAt(Instant respondedAt) {
        this.respondedAt = respondedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BookingQuote createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<BookingQuoteItem> getItems() {
        return this.items;
    }

    public void setItems(Set<BookingQuoteItem> bookingQuoteItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setQuote(null));
        }
        if (bookingQuoteItems != null) {
            bookingQuoteItems.forEach(i -> i.setQuote(this));
        }
        this.items = bookingQuoteItems;
    }

    public BookingQuote items(Set<BookingQuoteItem> bookingQuoteItems) {
        this.setItems(bookingQuoteItems);
        return this;
    }

    public BookingQuote addItem(BookingQuoteItem bookingQuoteItem) {
        this.items.add(bookingQuoteItem);
        bookingQuoteItem.setQuote(this);
        return this;
    }

    public BookingQuote removeItem(BookingQuoteItem bookingQuoteItem) {
        this.items.remove(bookingQuoteItem);
        bookingQuoteItem.setQuote(null);
        return this;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public BookingQuote professional(Professional professional) {
        this.setProfessional(professional);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BookingQuote booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingQuote)) {
            return false;
        }
        return getId() != null && getId().equals(((BookingQuote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingQuote{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", notes='" + getNotes() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", respondedAt='" + getRespondedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}

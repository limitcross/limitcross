package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.QuoteItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A BookingQuoteItem.
 */
@Entity
@Table(name = "booking_quote_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingQuoteItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private QuoteItemType itemType;

    @NotNull
    @Size(max = 200)
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "unit_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "line_total", precision = 21, scale = 2, nullable = false)
    private BigDecimal lineTotal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "items", "professional", "booking" }, allowSetters = true)
    private BookingQuote quote;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BookingQuoteItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuoteItemType getItemType() {
        return this.itemType;
    }

    public BookingQuoteItem itemType(QuoteItemType itemType) {
        this.setItemType(itemType);
        return this;
    }

    public void setItemType(QuoteItemType itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return this.description;
    }

    public BookingQuoteItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public BookingQuoteItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public BookingQuoteItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return this.lineTotal;
    }

    public BookingQuoteItem lineTotal(BigDecimal lineTotal) {
        this.setLineTotal(lineTotal);
        return this;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public BookingQuote getQuote() {
        return this.quote;
    }

    public void setQuote(BookingQuote bookingQuote) {
        this.quote = bookingQuote;
    }

    public BookingQuoteItem quote(BookingQuote bookingQuote) {
        this.setQuote(bookingQuote);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingQuoteItem)) {
            return false;
        }
        return getId() != null && getId().equals(((BookingQuoteItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingQuoteItem{" +
            "id=" + getId() +
            ", itemType='" + getItemType() + "'" +
            ", description='" + getDescription() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", quantity=" + getQuantity() +
            ", lineTotal=" + getLineTotal() +
            "}";
    }
}

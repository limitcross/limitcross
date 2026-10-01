package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.QuoteItemType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.BookingQuoteItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingQuoteItemDTO implements Serializable {

    private Long id;

    @NotNull
    private QuoteItemType itemType;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitPrice;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal lineTotal;

    @NotNull
    private BookingQuoteDTO quote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuoteItemType getItemType() {
        return itemType;
    }

    public void setItemType(QuoteItemType itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public BookingQuoteDTO getQuote() {
        return quote;
    }

    public void setQuote(BookingQuoteDTO quote) {
        this.quote = quote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingQuoteItemDTO)) {
            return false;
        }

        BookingQuoteItemDTO bookingQuoteItemDTO = (BookingQuoteItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingQuoteItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingQuoteItemDTO{" +
            "id=" + getId() +
            ", itemType='" + getItemType() + "'" +
            ", description='" + getDescription() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", quantity=" + getQuantity() +
            ", lineTotal=" + getLineTotal() +
            ", quote=" + getQuote() +
            "}";
    }
}

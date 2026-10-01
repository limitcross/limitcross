package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.InvoiceLine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceLineDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String description;

    @Size(max = 10)
    private String sacCode;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitPrice;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal gstPercent;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal lineTotal;

    @NotNull
    private InvoiceDTO invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSacCode() {
        return sacCode;
    }

    public void setSacCode(String sacCode) {
        this.sacCode = sacCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getGstPercent() {
        return gstPercent;
    }

    public void setGstPercent(BigDecimal gstPercent) {
        this.gstPercent = gstPercent;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceLineDTO)) {
            return false;
        }

        InvoiceLineDTO invoiceLineDTO = (InvoiceLineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceLineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceLineDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", sacCode='" + getSacCode() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", gstPercent=" + getGstPercent() +
            ", lineTotal=" + getLineTotal() +
            ", invoice=" + getInvoice() +
            "}";
    }
}

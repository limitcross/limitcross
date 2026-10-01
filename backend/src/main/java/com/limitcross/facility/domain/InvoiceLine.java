package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A InvoiceLine.
 */
@Entity
@Table(name = "invoice_line")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Size(max = 10)
    @Column(name = "sac_code", length = 10)
    private String sacCode;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "unit_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "gst_percent", precision = 21, scale = 2, nullable = false)
    private BigDecimal gstPercent;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "line_total", precision = 21, scale = 2, nullable = false)
    private BigDecimal lineTotal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "lines", "customer", "booking" }, allowSetters = true)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvoiceLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public InvoiceLine description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSacCode() {
        return this.sacCode;
    }

    public InvoiceLine sacCode(String sacCode) {
        this.setSacCode(sacCode);
        return this;
    }

    public void setSacCode(String sacCode) {
        this.sacCode = sacCode;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public InvoiceLine quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public InvoiceLine unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getGstPercent() {
        return this.gstPercent;
    }

    public InvoiceLine gstPercent(BigDecimal gstPercent) {
        this.setGstPercent(gstPercent);
        return this;
    }

    public void setGstPercent(BigDecimal gstPercent) {
        this.gstPercent = gstPercent;
    }

    public BigDecimal getLineTotal() {
        return this.lineTotal;
    }

    public InvoiceLine lineTotal(BigDecimal lineTotal) {
        this.setLineTotal(lineTotal);
        return this;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoiceLine invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceLine)) {
            return false;
        }
        return getId() != null && getId().equals(((InvoiceLine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceLine{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", sacCode='" + getSacCode() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", gstPercent=" + getGstPercent() +
            ", lineTotal=" + getLineTotal() +
            "}";
    }
}

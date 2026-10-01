package com.limitcross.facility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.limitcross.facility.domain.enumeration.InvoiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "invoice_no", length = 30, nullable = false, unique = true)
    private String invoiceNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", nullable = false)
    private InvoiceType invoiceType;

    @Size(max = 15)
    @Column(name = "customer_gstin", length = 15)
    private String customerGstin;

    @NotNull
    @Size(max = 15)
    @Column(name = "seller_gstin", length = 15, nullable = false)
    private String sellerGstin;

    @NotNull
    @Size(max = 40)
    @Column(name = "place_of_supply", length = 40, nullable = false)
    private String placeOfSupply;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "taxable_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal taxableAmount;

    @DecimalMin(value = "0")
    @Column(name = "cgst", precision = 21, scale = 2)
    private BigDecimal cgst;

    @DecimalMin(value = "0")
    @Column(name = "sgst", precision = 21, scale = 2)
    private BigDecimal sgst;

    @DecimalMin(value = "0")
    @Column(name = "igst", precision = 21, scale = 2)
    private BigDecimal igst;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Size(max = 255)
    @Column(name = "pdf_url", length = 255)
    private String pdfUrl;

    @Column(name = "issued_at")
    private Instant issuedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    @JsonIgnoreProperties(value = { "invoice" }, allowSetters = true)
    private Set<InvoiceLine> lines = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User customer;

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

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public Invoice invoiceNo(String invoiceNo) {
        this.setInvoiceNo(invoiceNo);
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public InvoiceType getInvoiceType() {
        return this.invoiceType;
    }

    public Invoice invoiceType(InvoiceType invoiceType) {
        this.setInvoiceType(invoiceType);
        return this;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getCustomerGstin() {
        return this.customerGstin;
    }

    public Invoice customerGstin(String customerGstin) {
        this.setCustomerGstin(customerGstin);
        return this;
    }

    public void setCustomerGstin(String customerGstin) {
        this.customerGstin = customerGstin;
    }

    public String getSellerGstin() {
        return this.sellerGstin;
    }

    public Invoice sellerGstin(String sellerGstin) {
        this.setSellerGstin(sellerGstin);
        return this;
    }

    public void setSellerGstin(String sellerGstin) {
        this.sellerGstin = sellerGstin;
    }

    public String getPlaceOfSupply() {
        return this.placeOfSupply;
    }

    public Invoice placeOfSupply(String placeOfSupply) {
        this.setPlaceOfSupply(placeOfSupply);
        return this;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public BigDecimal getTaxableAmount() {
        return this.taxableAmount;
    }

    public Invoice taxableAmount(BigDecimal taxableAmount) {
        this.setTaxableAmount(taxableAmount);
        return this;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getCgst() {
        return this.cgst;
    }

    public Invoice cgst(BigDecimal cgst) {
        this.setCgst(cgst);
        return this;
    }

    public void setCgst(BigDecimal cgst) {
        this.cgst = cgst;
    }

    public BigDecimal getSgst() {
        return this.sgst;
    }

    public Invoice sgst(BigDecimal sgst) {
        this.setSgst(sgst);
        return this;
    }

    public void setSgst(BigDecimal sgst) {
        this.sgst = sgst;
    }

    public BigDecimal getIgst() {
        return this.igst;
    }

    public Invoice igst(BigDecimal igst) {
        this.setIgst(igst);
        return this;
    }

    public void setIgst(BigDecimal igst) {
        this.igst = igst;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Invoice totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPdfUrl() {
        return this.pdfUrl;
    }

    public Invoice pdfUrl(String pdfUrl) {
        this.setPdfUrl(pdfUrl);
        return this;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public Instant getIssuedAt() {
        return this.issuedAt;
    }

    public Invoice issuedAt(Instant issuedAt) {
        this.setIssuedAt(issuedAt);
        return this;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Set<InvoiceLine> getLines() {
        return this.lines;
    }

    public void setLines(Set<InvoiceLine> invoiceLines) {
        if (this.lines != null) {
            this.lines.forEach(i -> i.setInvoice(null));
        }
        if (invoiceLines != null) {
            invoiceLines.forEach(i -> i.setInvoice(this));
        }
        this.lines = invoiceLines;
    }

    public Invoice lines(Set<InvoiceLine> invoiceLines) {
        this.setLines(invoiceLines);
        return this;
    }

    public Invoice addLine(InvoiceLine invoiceLine) {
        this.lines.add(invoiceLine);
        invoiceLine.setInvoice(this);
        return this;
    }

    public Invoice removeLine(InvoiceLine invoiceLine) {
        this.lines.remove(invoiceLine);
        invoiceLine.setInvoice(null);
        return this;
    }

    public User getCustomer() {
        return this.customer;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public Invoice customer(User user) {
        this.setCustomer(user);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Invoice booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceType='" + getInvoiceType() + "'" +
            ", customerGstin='" + getCustomerGstin() + "'" +
            ", sellerGstin='" + getSellerGstin() + "'" +
            ", placeOfSupply='" + getPlaceOfSupply() + "'" +
            ", taxableAmount=" + getTaxableAmount() +
            ", cgst=" + getCgst() +
            ", sgst=" + getSgst() +
            ", igst=" + getIgst() +
            ", totalAmount=" + getTotalAmount() +
            ", pdfUrl='" + getPdfUrl() + "'" +
            ", issuedAt='" + getIssuedAt() + "'" +
            "}";
    }
}

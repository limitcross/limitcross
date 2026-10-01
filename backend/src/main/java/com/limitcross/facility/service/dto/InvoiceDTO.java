package com.limitcross.facility.service.dto;

import com.limitcross.facility.domain.enumeration.InvoiceType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.Invoice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String invoiceNo;

    @NotNull
    private InvoiceType invoiceType;

    @Size(max = 15)
    private String customerGstin;

    @NotNull
    @Size(max = 15)
    private String sellerGstin;

    @NotNull
    @Size(max = 40)
    private String placeOfSupply;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal taxableAmount;

    @DecimalMin(value = "0")
    private BigDecimal cgst;

    @DecimalMin(value = "0")
    private BigDecimal sgst;

    @DecimalMin(value = "0")
    private BigDecimal igst;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalAmount;

    @Size(max = 255)
    private String pdfUrl;

    private Instant issuedAt;

    @NotNull
    private UserDTO customer;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getCustomerGstin() {
        return customerGstin;
    }

    public void setCustomerGstin(String customerGstin) {
        this.customerGstin = customerGstin;
    }

    public String getSellerGstin() {
        return sellerGstin;
    }

    public void setSellerGstin(String sellerGstin) {
        this.sellerGstin = sellerGstin;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getCgst() {
        return cgst;
    }

    public void setCgst(BigDecimal cgst) {
        this.cgst = cgst;
    }

    public BigDecimal getSgst() {
        return sgst;
    }

    public void setSgst(BigDecimal sgst) {
        this.sgst = sgst;
    }

    public BigDecimal getIgst() {
        return igst;
    }

    public void setIgst(BigDecimal igst) {
        this.igst = igst;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public UserDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserDTO customer) {
        this.customer = customer;
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
        if (!(o instanceof InvoiceDTO)) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceDTO{" +
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
            ", customer=" + getCustomer() +
            ", booking=" + getBooking() +
            "}";
    }
}

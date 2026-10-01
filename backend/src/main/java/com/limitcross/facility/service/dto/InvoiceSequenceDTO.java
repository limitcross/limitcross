package com.limitcross.facility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.limitcross.facility.domain.InvoiceSequence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceSequenceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String series;

    @NotNull
    @Size(max = 9)
    private String fiscalYear;

    @NotNull
    @Min(value = 0L)
    private Long lastNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Long getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(Long lastNumber) {
        this.lastNumber = lastNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceSequenceDTO)) {
            return false;
        }

        InvoiceSequenceDTO invoiceSequenceDTO = (InvoiceSequenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceSequenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceSequenceDTO{" +
            "id=" + getId() +
            ", series='" + getSeries() + "'" +
            ", fiscalYear='" + getFiscalYear() + "'" +
            ", lastNumber=" + getLastNumber() +
            "}";
    }
}

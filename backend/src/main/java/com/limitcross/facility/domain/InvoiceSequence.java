package com.limitcross.facility.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A InvoiceSequence.
 */
@Entity
@Table(name = "invoice_sequence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceSequence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "series", length = 10, nullable = false)
    private String series;

    @NotNull
    @Size(max = 9)
    @Column(name = "fiscal_year", length = 9, nullable = false)
    private String fiscalYear;

    @NotNull
    @Min(value = 0L)
    @Column(name = "last_number", nullable = false)
    private Long lastNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvoiceSequence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeries() {
        return this.series;
    }

    public InvoiceSequence series(String series) {
        this.setSeries(series);
        return this;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getFiscalYear() {
        return this.fiscalYear;
    }

    public InvoiceSequence fiscalYear(String fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Long getLastNumber() {
        return this.lastNumber;
    }

    public InvoiceSequence lastNumber(Long lastNumber) {
        this.setLastNumber(lastNumber);
        return this;
    }

    public void setLastNumber(Long lastNumber) {
        this.lastNumber = lastNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceSequence)) {
            return false;
        }
        return getId() != null && getId().equals(((InvoiceSequence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceSequence{" +
            "id=" + getId() +
            ", series='" + getSeries() + "'" +
            ", fiscalYear='" + getFiscalYear() + "'" +
            ", lastNumber=" + getLastNumber() +
            "}";
    }
}

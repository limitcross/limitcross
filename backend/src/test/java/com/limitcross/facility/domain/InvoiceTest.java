package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.InvoiceLineTestSamples.*;
import static com.limitcross.facility.domain.InvoiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void lineTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        InvoiceLine invoiceLineBack = getInvoiceLineRandomSampleGenerator();

        invoice.addLine(invoiceLineBack);
        assertThat(invoice.getLines()).containsOnly(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isEqualTo(invoice);

        invoice.removeLine(invoiceLineBack);
        assertThat(invoice.getLines()).doesNotContain(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isNull();

        invoice.lines(new HashSet<>(Set.of(invoiceLineBack)));
        assertThat(invoice.getLines()).containsOnly(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isEqualTo(invoice);

        invoice.setLines(new HashSet<>());
        assertThat(invoice.getLines()).doesNotContain(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isNull();
    }

    @Test
    void bookingTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        invoice.setBooking(bookingBack);
        assertThat(invoice.getBooking()).isEqualTo(bookingBack);

        invoice.booking(null);
        assertThat(invoice.getBooking()).isNull();
    }
}

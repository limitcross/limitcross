package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.InvoiceSequenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceSequenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceSequence.class);
        InvoiceSequence invoiceSequence1 = getInvoiceSequenceSample1();
        InvoiceSequence invoiceSequence2 = new InvoiceSequence();
        assertThat(invoiceSequence1).isNotEqualTo(invoiceSequence2);

        invoiceSequence2.setId(invoiceSequence1.getId());
        assertThat(invoiceSequence1).isEqualTo(invoiceSequence2);

        invoiceSequence2 = getInvoiceSequenceSample2();
        assertThat(invoiceSequence1).isNotEqualTo(invoiceSequence2);
    }
}

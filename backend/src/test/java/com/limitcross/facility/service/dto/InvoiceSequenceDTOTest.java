package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceSequenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceSequenceDTO.class);
        InvoiceSequenceDTO invoiceSequenceDTO1 = new InvoiceSequenceDTO();
        invoiceSequenceDTO1.setId(1L);
        InvoiceSequenceDTO invoiceSequenceDTO2 = new InvoiceSequenceDTO();
        assertThat(invoiceSequenceDTO1).isNotEqualTo(invoiceSequenceDTO2);
        invoiceSequenceDTO2.setId(invoiceSequenceDTO1.getId());
        assertThat(invoiceSequenceDTO1).isEqualTo(invoiceSequenceDTO2);
        invoiceSequenceDTO2.setId(2L);
        assertThat(invoiceSequenceDTO1).isNotEqualTo(invoiceSequenceDTO2);
        invoiceSequenceDTO1.setId(null);
        assertThat(invoiceSequenceDTO1).isNotEqualTo(invoiceSequenceDTO2);
    }
}

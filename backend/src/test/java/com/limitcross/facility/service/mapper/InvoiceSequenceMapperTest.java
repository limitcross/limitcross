package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.InvoiceSequenceAsserts.*;
import static com.limitcross.facility.domain.InvoiceSequenceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvoiceSequenceMapperTest {

    private InvoiceSequenceMapper invoiceSequenceMapper;

    @BeforeEach
    void setUp() {
        invoiceSequenceMapper = new InvoiceSequenceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInvoiceSequenceSample1();
        var actual = invoiceSequenceMapper.toEntity(invoiceSequenceMapper.toDto(expected));
        assertInvoiceSequenceAllPropertiesEquals(expected, actual);
    }
}

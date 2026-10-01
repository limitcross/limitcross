package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.RefundAsserts.*;
import static com.limitcross.facility.domain.RefundTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefundMapperTest {

    private RefundMapper refundMapper;

    @BeforeEach
    void setUp() {
        refundMapper = new RefundMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRefundSample1();
        var actual = refundMapper.toEntity(refundMapper.toDto(expected));
        assertRefundAllPropertiesEquals(expected, actual);
    }
}

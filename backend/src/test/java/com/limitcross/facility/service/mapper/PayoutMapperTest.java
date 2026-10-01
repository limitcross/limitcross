package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.PayoutAsserts.*;
import static com.limitcross.facility.domain.PayoutTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PayoutMapperTest {

    private PayoutMapper payoutMapper;

    @BeforeEach
    void setUp() {
        payoutMapper = new PayoutMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPayoutSample1();
        var actual = payoutMapper.toEntity(payoutMapper.toDto(expected));
        assertPayoutAllPropertiesEquals(expected, actual);
    }
}

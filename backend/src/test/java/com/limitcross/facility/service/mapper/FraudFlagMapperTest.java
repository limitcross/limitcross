package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.FraudFlagAsserts.*;
import static com.limitcross.facility.domain.FraudFlagTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FraudFlagMapperTest {

    private FraudFlagMapper fraudFlagMapper;

    @BeforeEach
    void setUp() {
        fraudFlagMapper = new FraudFlagMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFraudFlagSample1();
        var actual = fraudFlagMapper.toEntity(fraudFlagMapper.toDto(expected));
        assertFraudFlagAllPropertiesEquals(expected, actual);
    }
}

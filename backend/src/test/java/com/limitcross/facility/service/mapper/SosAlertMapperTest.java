package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.SosAlertAsserts.*;
import static com.limitcross.facility.domain.SosAlertTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SosAlertMapperTest {

    private SosAlertMapper sosAlertMapper;

    @BeforeEach
    void setUp() {
        sosAlertMapper = new SosAlertMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSosAlertSample1();
        var actual = sosAlertMapper.toEntity(sosAlertMapper.toDto(expected));
        assertSosAlertAllPropertiesEquals(expected, actual);
    }
}

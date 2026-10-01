package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.CallSessionAsserts.*;
import static com.limitcross.facility.domain.CallSessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CallSessionMapperTest {

    private CallSessionMapper callSessionMapper;

    @BeforeEach
    void setUp() {
        callSessionMapper = new CallSessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCallSessionSample1();
        var actual = callSessionMapper.toEntity(callSessionMapper.toDto(expected));
        assertCallSessionAllPropertiesEquals(expected, actual);
    }
}

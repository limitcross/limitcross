package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ApiIdempotencyKeyAsserts.*;
import static com.limitcross.facility.domain.ApiIdempotencyKeyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApiIdempotencyKeyMapperTest {

    private ApiIdempotencyKeyMapper apiIdempotencyKeyMapper;

    @BeforeEach
    void setUp() {
        apiIdempotencyKeyMapper = new ApiIdempotencyKeyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApiIdempotencyKeySample1();
        var actual = apiIdempotencyKeyMapper.toEntity(apiIdempotencyKeyMapper.toDto(expected));
        assertApiIdempotencyKeyAllPropertiesEquals(expected, actual);
    }
}

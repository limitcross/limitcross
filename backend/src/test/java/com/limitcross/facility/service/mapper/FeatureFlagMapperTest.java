package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.FeatureFlagAsserts.*;
import static com.limitcross.facility.domain.FeatureFlagTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeatureFlagMapperTest {

    private FeatureFlagMapper featureFlagMapper;

    @BeforeEach
    void setUp() {
        featureFlagMapper = new FeatureFlagMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFeatureFlagSample1();
        var actual = featureFlagMapper.toEntity(featureFlagMapper.toDto(expected));
        assertFeatureFlagAllPropertiesEquals(expected, actual);
    }
}

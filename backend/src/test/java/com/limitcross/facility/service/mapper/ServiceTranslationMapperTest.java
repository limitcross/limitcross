package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ServiceTranslationAsserts.*;
import static com.limitcross.facility.domain.ServiceTranslationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceTranslationMapperTest {

    private ServiceTranslationMapper serviceTranslationMapper;

    @BeforeEach
    void setUp() {
        serviceTranslationMapper = new ServiceTranslationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceTranslationSample1();
        var actual = serviceTranslationMapper.toEntity(serviceTranslationMapper.toDto(expected));
        assertServiceTranslationAllPropertiesEquals(expected, actual);
    }
}

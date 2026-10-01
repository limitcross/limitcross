package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.AppConfigAsserts.*;
import static com.limitcross.facility.domain.AppConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppConfigMapperTest {

    private AppConfigMapper appConfigMapper;

    @BeforeEach
    void setUp() {
        appConfigMapper = new AppConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppConfigSample1();
        var actual = appConfigMapper.toEntity(appConfigMapper.toDto(expected));
        assertAppConfigAllPropertiesEquals(expected, actual);
    }
}

package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.CityDailyMetricsAsserts.*;
import static com.limitcross.facility.domain.CityDailyMetricsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CityDailyMetricsMapperTest {

    private CityDailyMetricsMapper cityDailyMetricsMapper;

    @BeforeEach
    void setUp() {
        cityDailyMetricsMapper = new CityDailyMetricsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCityDailyMetricsSample1();
        var actual = cityDailyMetricsMapper.toEntity(cityDailyMetricsMapper.toDto(expected));
        assertCityDailyMetricsAllPropertiesEquals(expected, actual);
    }
}

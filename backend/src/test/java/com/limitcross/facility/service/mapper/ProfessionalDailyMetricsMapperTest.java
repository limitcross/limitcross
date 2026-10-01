package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalDailyMetricsAsserts.*;
import static com.limitcross.facility.domain.ProfessionalDailyMetricsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalDailyMetricsMapperTest {

    private ProfessionalDailyMetricsMapper professionalDailyMetricsMapper;

    @BeforeEach
    void setUp() {
        professionalDailyMetricsMapper = new ProfessionalDailyMetricsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalDailyMetricsSample1();
        var actual = professionalDailyMetricsMapper.toEntity(professionalDailyMetricsMapper.toDto(expected));
        assertProfessionalDailyMetricsAllPropertiesEquals(expected, actual);
    }
}

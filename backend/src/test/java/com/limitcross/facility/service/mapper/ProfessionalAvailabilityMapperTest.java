package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalAvailabilityAsserts.*;
import static com.limitcross.facility.domain.ProfessionalAvailabilityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalAvailabilityMapperTest {

    private ProfessionalAvailabilityMapper professionalAvailabilityMapper;

    @BeforeEach
    void setUp() {
        professionalAvailabilityMapper = new ProfessionalAvailabilityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalAvailabilitySample1();
        var actual = professionalAvailabilityMapper.toEntity(professionalAvailabilityMapper.toDto(expected));
        assertProfessionalAvailabilityAllPropertiesEquals(expected, actual);
    }
}

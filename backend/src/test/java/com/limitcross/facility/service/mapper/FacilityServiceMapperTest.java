package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.FacilityServiceAsserts.*;
import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacilityServiceMapperTest {

    private FacilityServiceMapper facilityServiceMapper;

    @BeforeEach
    void setUp() {
        facilityServiceMapper = new FacilityServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFacilityServiceSample1();
        var actual = facilityServiceMapper.toEntity(facilityServiceMapper.toDto(expected));
        assertFacilityServiceAllPropertiesEquals(expected, actual);
    }
}

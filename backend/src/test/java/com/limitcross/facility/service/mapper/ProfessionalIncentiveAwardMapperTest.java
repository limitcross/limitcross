package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalIncentiveAwardAsserts.*;
import static com.limitcross.facility.domain.ProfessionalIncentiveAwardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalIncentiveAwardMapperTest {

    private ProfessionalIncentiveAwardMapper professionalIncentiveAwardMapper;

    @BeforeEach
    void setUp() {
        professionalIncentiveAwardMapper = new ProfessionalIncentiveAwardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalIncentiveAwardSample1();
        var actual = professionalIncentiveAwardMapper.toEntity(professionalIncentiveAwardMapper.toDto(expected));
        assertProfessionalIncentiveAwardAllPropertiesEquals(expected, actual);
    }
}

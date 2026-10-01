package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalTimeOffAsserts.*;
import static com.limitcross.facility.domain.ProfessionalTimeOffTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalTimeOffMapperTest {

    private ProfessionalTimeOffMapper professionalTimeOffMapper;

    @BeforeEach
    void setUp() {
        professionalTimeOffMapper = new ProfessionalTimeOffMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalTimeOffSample1();
        var actual = professionalTimeOffMapper.toEntity(professionalTimeOffMapper.toDto(expected));
        assertProfessionalTimeOffAllPropertiesEquals(expected, actual);
    }
}

package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalLocationLogAsserts.*;
import static com.limitcross.facility.domain.ProfessionalLocationLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalLocationLogMapperTest {

    private ProfessionalLocationLogMapper professionalLocationLogMapper;

    @BeforeEach
    void setUp() {
        professionalLocationLogMapper = new ProfessionalLocationLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalLocationLogSample1();
        var actual = professionalLocationLogMapper.toEntity(professionalLocationLogMapper.toDto(expected));
        assertProfessionalLocationLogAllPropertiesEquals(expected, actual);
    }
}

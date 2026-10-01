package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalTrainingAsserts.*;
import static com.limitcross.facility.domain.ProfessionalTrainingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalTrainingMapperTest {

    private ProfessionalTrainingMapper professionalTrainingMapper;

    @BeforeEach
    void setUp() {
        professionalTrainingMapper = new ProfessionalTrainingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalTrainingSample1();
        var actual = professionalTrainingMapper.toEntity(professionalTrainingMapper.toDto(expected));
        assertProfessionalTrainingAllPropertiesEquals(expected, actual);
    }
}

package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalSkillAsserts.*;
import static com.limitcross.facility.domain.ProfessionalSkillTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalSkillMapperTest {

    private ProfessionalSkillMapper professionalSkillMapper;

    @BeforeEach
    void setUp() {
        professionalSkillMapper = new ProfessionalSkillMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalSkillSample1();
        var actual = professionalSkillMapper.toEntity(professionalSkillMapper.toDto(expected));
        assertProfessionalSkillAllPropertiesEquals(expected, actual);
    }
}

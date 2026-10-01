package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalTierAsserts.*;
import static com.limitcross.facility.domain.ProfessionalTierTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalTierMapperTest {

    private ProfessionalTierMapper professionalTierMapper;

    @BeforeEach
    void setUp() {
        professionalTierMapper = new ProfessionalTierMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalTierSample1();
        var actual = professionalTierMapper.toEntity(professionalTierMapper.toDto(expected));
        assertProfessionalTierAllPropertiesEquals(expected, actual);
    }
}

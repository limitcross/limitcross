package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalTierHistoryAsserts.*;
import static com.limitcross.facility.domain.ProfessionalTierHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalTierHistoryMapperTest {

    private ProfessionalTierHistoryMapper professionalTierHistoryMapper;

    @BeforeEach
    void setUp() {
        professionalTierHistoryMapper = new ProfessionalTierHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalTierHistorySample1();
        var actual = professionalTierHistoryMapper.toEntity(professionalTierHistoryMapper.toDto(expected));
        assertProfessionalTierHistoryAllPropertiesEquals(expected, actual);
    }
}

package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalWalletAsserts.*;
import static com.limitcross.facility.domain.ProfessionalWalletTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalWalletMapperTest {

    private ProfessionalWalletMapper professionalWalletMapper;

    @BeforeEach
    void setUp() {
        professionalWalletMapper = new ProfessionalWalletMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalWalletSample1();
        var actual = professionalWalletMapper.toEntity(professionalWalletMapper.toDto(expected));
        assertProfessionalWalletAllPropertiesEquals(expected, actual);
    }
}

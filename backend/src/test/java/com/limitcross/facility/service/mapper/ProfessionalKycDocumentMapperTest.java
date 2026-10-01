package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ProfessionalKycDocumentAsserts.*;
import static com.limitcross.facility.domain.ProfessionalKycDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalKycDocumentMapperTest {

    private ProfessionalKycDocumentMapper professionalKycDocumentMapper;

    @BeforeEach
    void setUp() {
        professionalKycDocumentMapper = new ProfessionalKycDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalKycDocumentSample1();
        var actual = professionalKycDocumentMapper.toEntity(professionalKycDocumentMapper.toDto(expected));
        assertProfessionalKycDocumentAllPropertiesEquals(expected, actual);
    }
}

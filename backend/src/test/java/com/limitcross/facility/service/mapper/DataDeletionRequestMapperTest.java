package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.DataDeletionRequestAsserts.*;
import static com.limitcross.facility.domain.DataDeletionRequestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataDeletionRequestMapperTest {

    private DataDeletionRequestMapper dataDeletionRequestMapper;

    @BeforeEach
    void setUp() {
        dataDeletionRequestMapper = new DataDeletionRequestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDataDeletionRequestSample1();
        var actual = dataDeletionRequestMapper.toEntity(dataDeletionRequestMapper.toDto(expected));
        assertDataDeletionRequestAllPropertiesEquals(expected, actual);
    }
}

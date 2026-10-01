package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.WarrantyClaimAsserts.*;
import static com.limitcross.facility.domain.WarrantyClaimTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WarrantyClaimMapperTest {

    private WarrantyClaimMapper warrantyClaimMapper;

    @BeforeEach
    void setUp() {
        warrantyClaimMapper = new WarrantyClaimMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWarrantyClaimSample1();
        var actual = warrantyClaimMapper.toEntity(warrantyClaimMapper.toDto(expected));
        assertWarrantyClaimAllPropertiesEquals(expected, actual);
    }
}

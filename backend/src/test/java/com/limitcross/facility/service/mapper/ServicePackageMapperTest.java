package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ServicePackageAsserts.*;
import static com.limitcross.facility.domain.ServicePackageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicePackageMapperTest {

    private ServicePackageMapper servicePackageMapper;

    @BeforeEach
    void setUp() {
        servicePackageMapper = new ServicePackageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServicePackageSample1();
        var actual = servicePackageMapper.toEntity(servicePackageMapper.toDto(expected));
        assertServicePackageAllPropertiesEquals(expected, actual);
    }
}

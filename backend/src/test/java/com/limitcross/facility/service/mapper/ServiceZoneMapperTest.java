package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ServiceZoneAsserts.*;
import static com.limitcross.facility.domain.ServiceZoneTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceZoneMapperTest {

    private ServiceZoneMapper serviceZoneMapper;

    @BeforeEach
    void setUp() {
        serviceZoneMapper = new ServiceZoneMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceZoneSample1();
        var actual = serviceZoneMapper.toEntity(serviceZoneMapper.toDto(expected));
        assertServiceZoneAllPropertiesEquals(expected, actual);
    }
}

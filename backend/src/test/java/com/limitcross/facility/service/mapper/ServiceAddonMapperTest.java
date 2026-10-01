package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.ServiceAddonAsserts.*;
import static com.limitcross.facility.domain.ServiceAddonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceAddonMapperTest {

    private ServiceAddonMapper serviceAddonMapper;

    @BeforeEach
    void setUp() {
        serviceAddonMapper = new ServiceAddonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceAddonSample1();
        var actual = serviceAddonMapper.toEntity(serviceAddonMapper.toDto(expected));
        assertServiceAddonAllPropertiesEquals(expected, actual);
    }
}

package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.CityPackagePriceAsserts.*;
import static com.limitcross.facility.domain.CityPackagePriceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CityPackagePriceMapperTest {

    private CityPackagePriceMapper cityPackagePriceMapper;

    @BeforeEach
    void setUp() {
        cityPackagePriceMapper = new CityPackagePriceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCityPackagePriceSample1();
        var actual = cityPackagePriceMapper.toEntity(cityPackagePriceMapper.toDto(expected));
        assertCityPackagePriceAllPropertiesEquals(expected, actual);
    }
}

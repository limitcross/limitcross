package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.BannerAsserts.*;
import static com.limitcross.facility.domain.BannerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BannerMapperTest {

    private BannerMapper bannerMapper;

    @BeforeEach
    void setUp() {
        bannerMapper = new BannerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBannerSample1();
        var actual = bannerMapper.toEntity(bannerMapper.toDto(expected));
        assertBannerAllPropertiesEquals(expected, actual);
    }
}

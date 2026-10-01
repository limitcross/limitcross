package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.CouponRedemptionAsserts.*;
import static com.limitcross.facility.domain.CouponRedemptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CouponRedemptionMapperTest {

    private CouponRedemptionMapper couponRedemptionMapper;

    @BeforeEach
    void setUp() {
        couponRedemptionMapper = new CouponRedemptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCouponRedemptionSample1();
        var actual = couponRedemptionMapper.toEntity(couponRedemptionMapper.toDto(expected));
        assertCouponRedemptionAllPropertiesEquals(expected, actual);
    }
}

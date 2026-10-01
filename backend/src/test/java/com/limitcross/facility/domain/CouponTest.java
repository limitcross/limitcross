package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.CouponTestSamples.*;
import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CouponTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coupon.class);
        Coupon coupon1 = getCouponSample1();
        Coupon coupon2 = new Coupon();
        assertThat(coupon1).isNotEqualTo(coupon2);

        coupon2.setId(coupon1.getId());
        assertThat(coupon1).isEqualTo(coupon2);

        coupon2 = getCouponSample2();
        assertThat(coupon1).isNotEqualTo(coupon2);
    }

    @Test
    void serviceTest() {
        Coupon coupon = getCouponRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        coupon.setService(facilityServiceBack);
        assertThat(coupon.getService()).isEqualTo(facilityServiceBack);

        coupon.service(null);
        assertThat(coupon.getService()).isNull();
    }

    @Test
    void cityTest() {
        Coupon coupon = getCouponRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        coupon.setCity(cityBack);
        assertThat(coupon.getCity()).isEqualTo(cityBack);

        coupon.city(null);
        assertThat(coupon.getCity()).isNull();
    }
}

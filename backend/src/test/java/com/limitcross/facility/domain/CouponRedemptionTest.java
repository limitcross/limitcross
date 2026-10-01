package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.CouponRedemptionTestSamples.*;
import static com.limitcross.facility.domain.CouponTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CouponRedemptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouponRedemption.class);
        CouponRedemption couponRedemption1 = getCouponRedemptionSample1();
        CouponRedemption couponRedemption2 = new CouponRedemption();
        assertThat(couponRedemption1).isNotEqualTo(couponRedemption2);

        couponRedemption2.setId(couponRedemption1.getId());
        assertThat(couponRedemption1).isEqualTo(couponRedemption2);

        couponRedemption2 = getCouponRedemptionSample2();
        assertThat(couponRedemption1).isNotEqualTo(couponRedemption2);
    }

    @Test
    void bookingTest() {
        CouponRedemption couponRedemption = getCouponRedemptionRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        couponRedemption.setBooking(bookingBack);
        assertThat(couponRedemption.getBooking()).isEqualTo(bookingBack);

        couponRedemption.booking(null);
        assertThat(couponRedemption.getBooking()).isNull();
    }

    @Test
    void couponTest() {
        CouponRedemption couponRedemption = getCouponRedemptionRandomSampleGenerator();
        Coupon couponBack = getCouponRandomSampleGenerator();

        couponRedemption.setCoupon(couponBack);
        assertThat(couponRedemption.getCoupon()).isEqualTo(couponBack);

        couponRedemption.coupon(null);
        assertThat(couponRedemption.getCoupon()).isNull();
    }
}

package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CouponRedemptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CouponRedemption getCouponRedemptionSample1() {
        return new CouponRedemption().id(1L);
    }

    public static CouponRedemption getCouponRedemptionSample2() {
        return new CouponRedemption().id(2L);
    }

    public static CouponRedemption getCouponRedemptionRandomSampleGenerator() {
        return new CouponRedemption().id(longCount.incrementAndGet());
    }
}

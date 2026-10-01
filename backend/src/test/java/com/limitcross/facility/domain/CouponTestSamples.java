package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CouponTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Coupon getCouponSample1() {
        return new Coupon().id(1L).code("code1").description("description1").totalUsageLimit(1).perUserLimit(1).usedCount(1);
    }

    public static Coupon getCouponSample2() {
        return new Coupon().id(2L).code("code2").description("description2").totalUsageLimit(2).perUserLimit(2).usedCount(2);
    }

    public static Coupon getCouponRandomSampleGenerator() {
        return new Coupon()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .totalUsageLimit(intCount.incrementAndGet())
            .perUserLimit(intCount.incrementAndGet())
            .usedCount(intCount.incrementAndGet());
    }
}

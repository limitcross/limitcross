package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApiIdempotencyKeyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ApiIdempotencyKey getApiIdempotencyKeySample1() {
        return new ApiIdempotencyKey().id(1L).idemKey("idemKey1").requestHash("requestHash1").responseCode(1);
    }

    public static ApiIdempotencyKey getApiIdempotencyKeySample2() {
        return new ApiIdempotencyKey().id(2L).idemKey("idemKey2").requestHash("requestHash2").responseCode(2);
    }

    public static ApiIdempotencyKey getApiIdempotencyKeyRandomSampleGenerator() {
        return new ApiIdempotencyKey()
            .id(longCount.incrementAndGet())
            .idemKey(UUID.randomUUID().toString())
            .requestHash(UUID.randomUUID().toString())
            .responseCode(intCount.incrementAndGet());
    }
}

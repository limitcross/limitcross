package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RefundTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Refund getRefundSample1() {
        return new Refund().id(1L).reason("reason1").gatewayRefundId("gatewayRefundId1");
    }

    public static Refund getRefundSample2() {
        return new Refund().id(2L).reason("reason2").gatewayRefundId("gatewayRefundId2");
    }

    public static Refund getRefundRandomSampleGenerator() {
        return new Refund()
            .id(longCount.incrementAndGet())
            .reason(UUID.randomUUID().toString())
            .gatewayRefundId(UUID.randomUUID().toString());
    }
}

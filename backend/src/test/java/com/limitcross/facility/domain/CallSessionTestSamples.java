package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CallSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CallSession getCallSessionSample1() {
        return new CallSession().id(1L).virtualNumber("virtualNumber1").providerCallId("providerCallId1").durationSec(1);
    }

    public static CallSession getCallSessionSample2() {
        return new CallSession().id(2L).virtualNumber("virtualNumber2").providerCallId("providerCallId2").durationSec(2);
    }

    public static CallSession getCallSessionRandomSampleGenerator() {
        return new CallSession()
            .id(longCount.incrementAndGet())
            .virtualNumber(UUID.randomUUID().toString())
            .providerCallId(UUID.randomUUID().toString())
            .durationSec(intCount.incrementAndGet());
    }
}

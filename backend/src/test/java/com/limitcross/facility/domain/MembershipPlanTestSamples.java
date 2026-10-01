package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MembershipPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MembershipPlan getMembershipPlanSample1() {
        return new MembershipPlan().id(1L).name("name1").durationDays(1);
    }

    public static MembershipPlan getMembershipPlanSample2() {
        return new MembershipPlan().id(2L).name("name2").durationDays(2);
    }

    public static MembershipPlan getMembershipPlanRandomSampleGenerator() {
        return new MembershipPlan()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .durationDays(intCount.incrementAndGet());
    }
}

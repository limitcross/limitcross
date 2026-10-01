package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SlotCapacityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SlotCapacity getSlotCapacitySample1() {
        return new SlotCapacity().id(1L).capacity(1).bookedCount(1);
    }

    public static SlotCapacity getSlotCapacitySample2() {
        return new SlotCapacity().id(2L).capacity(2).bookedCount(2);
    }

    public static SlotCapacity getSlotCapacityRandomSampleGenerator() {
        return new SlotCapacity()
            .id(longCount.incrementAndGet())
            .capacity(intCount.incrementAndGet())
            .bookedCount(intCount.incrementAndGet());
    }
}

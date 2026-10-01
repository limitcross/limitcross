package com.limitcross.facility.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SlotHoldTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SlotHold getSlotHoldSample1() {
        return new SlotHold().id(1L);
    }

    public static SlotHold getSlotHoldSample2() {
        return new SlotHold().id(2L);
    }

    public static SlotHold getSlotHoldRandomSampleGenerator() {
        return new SlotHold().id(longCount.incrementAndGet());
    }
}

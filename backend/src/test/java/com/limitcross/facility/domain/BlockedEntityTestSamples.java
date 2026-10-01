package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BlockedEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BlockedEntity getBlockedEntitySample1() {
        return new BlockedEntity().id(1L).valueHash("valueHash1").reason("reason1");
    }

    public static BlockedEntity getBlockedEntitySample2() {
        return new BlockedEntity().id(2L).valueHash("valueHash2").reason("reason2");
    }

    public static BlockedEntity getBlockedEntityRandomSampleGenerator() {
        return new BlockedEntity()
            .id(longCount.incrementAndGet())
            .valueHash(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString());
    }
}

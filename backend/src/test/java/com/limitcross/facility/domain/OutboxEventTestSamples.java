package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OutboxEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OutboxEvent getOutboxEventSample1() {
        return new OutboxEvent().id(1L).aggregateType("aggregateType1").aggregateId("aggregateId1").eventType("eventType1").attempts(1);
    }

    public static OutboxEvent getOutboxEventSample2() {
        return new OutboxEvent().id(2L).aggregateType("aggregateType2").aggregateId("aggregateId2").eventType("eventType2").attempts(2);
    }

    public static OutboxEvent getOutboxEventRandomSampleGenerator() {
        return new OutboxEvent()
            .id(longCount.incrementAndGet())
            .aggregateType(UUID.randomUUID().toString())
            .aggregateId(UUID.randomUUID().toString())
            .eventType(UUID.randomUUID().toString())
            .attempts(intCount.incrementAndGet());
    }
}

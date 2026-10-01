package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentWebhookEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentWebhookEvent getPaymentWebhookEventSample1() {
        return new PaymentWebhookEvent().id(1L).eventId("eventId1").eventType("eventType1");
    }

    public static PaymentWebhookEvent getPaymentWebhookEventSample2() {
        return new PaymentWebhookEvent().id(2L).eventId("eventId2").eventType("eventType2");
    }

    public static PaymentWebhookEvent getPaymentWebhookEventRandomSampleGenerator() {
        return new PaymentWebhookEvent()
            .id(longCount.incrementAndGet())
            .eventId(UUID.randomUUID().toString())
            .eventType(UUID.randomUUID().toString());
    }
}

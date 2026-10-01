package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookingSubscription getBookingSubscriptionSample1() {
        return new BookingSubscription().id(1L).daysOfWeek("daysOfWeek1").preferredTime("preferredTime1");
    }

    public static BookingSubscription getBookingSubscriptionSample2() {
        return new BookingSubscription().id(2L).daysOfWeek("daysOfWeek2").preferredTime("preferredTime2");
    }

    public static BookingSubscription getBookingSubscriptionRandomSampleGenerator() {
        return new BookingSubscription()
            .id(longCount.incrementAndGet())
            .daysOfWeek(UUID.randomUUID().toString())
            .preferredTime(UUID.randomUUID().toString());
    }
}

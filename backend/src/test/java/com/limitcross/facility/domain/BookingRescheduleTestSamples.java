package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingRescheduleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookingReschedule getBookingRescheduleSample1() {
        return new BookingReschedule().id(1L).reason("reason1");
    }

    public static BookingReschedule getBookingRescheduleSample2() {
        return new BookingReschedule().id(2L).reason("reason2");
    }

    public static BookingReschedule getBookingRescheduleRandomSampleGenerator() {
        return new BookingReschedule().id(longCount.incrementAndGet()).reason(UUID.randomUUID().toString());
    }
}

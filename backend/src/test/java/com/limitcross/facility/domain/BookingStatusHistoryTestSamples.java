package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingStatusHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookingStatusHistory getBookingStatusHistorySample1() {
        return new BookingStatusHistory().id(1L).changedBy("changedBy1").note("note1");
    }

    public static BookingStatusHistory getBookingStatusHistorySample2() {
        return new BookingStatusHistory().id(2L).changedBy("changedBy2").note("note2");
    }

    public static BookingStatusHistory getBookingStatusHistoryRandomSampleGenerator() {
        return new BookingStatusHistory()
            .id(longCount.incrementAndGet())
            .changedBy(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString());
    }
}

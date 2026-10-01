package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookingAssignment getBookingAssignmentSample1() {
        return new BookingAssignment().id(1L).rejectReason("rejectReason1");
    }

    public static BookingAssignment getBookingAssignmentSample2() {
        return new BookingAssignment().id(2L).rejectReason("rejectReason2");
    }

    public static BookingAssignment getBookingAssignmentRandomSampleGenerator() {
        return new BookingAssignment().id(longCount.incrementAndGet()).rejectReason(UUID.randomUUID().toString());
    }
}

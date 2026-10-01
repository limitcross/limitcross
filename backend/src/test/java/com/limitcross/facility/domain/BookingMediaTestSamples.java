package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingMediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookingMedia getBookingMediaSample1() {
        return new BookingMedia().id(1L).fileUrl("fileUrl1");
    }

    public static BookingMedia getBookingMediaSample2() {
        return new BookingMedia().id(2L).fileUrl("fileUrl2");
    }

    public static BookingMedia getBookingMediaRandomSampleGenerator() {
        return new BookingMedia().id(longCount.incrementAndGet()).fileUrl(UUID.randomUUID().toString());
    }
}

package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingQuoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookingQuote getBookingQuoteSample1() {
        return new BookingQuote().id(1L).notes("notes1");
    }

    public static BookingQuote getBookingQuoteSample2() {
        return new BookingQuote().id(2L).notes("notes2");
    }

    public static BookingQuote getBookingQuoteRandomSampleGenerator() {
        return new BookingQuote().id(longCount.incrementAndGet()).notes(UUID.randomUUID().toString());
    }
}

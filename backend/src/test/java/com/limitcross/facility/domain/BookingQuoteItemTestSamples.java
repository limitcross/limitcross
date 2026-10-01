package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookingQuoteItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookingQuoteItem getBookingQuoteItemSample1() {
        return new BookingQuoteItem().id(1L).description("description1");
    }

    public static BookingQuoteItem getBookingQuoteItemSample2() {
        return new BookingQuoteItem().id(2L).description("description2");
    }

    public static BookingQuoteItem getBookingQuoteItemRandomSampleGenerator() {
        return new BookingQuoteItem().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}

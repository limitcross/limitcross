package com.limitcross.facility.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BookingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Booking getBookingSample1() {
        return new Booking()
            .id(1L)
            .bookingNo("bookingNo1")
            .publicId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .serviceTitle("serviceTitle1")
            .currency("currency1")
            .customerNotes("customerNotes1")
            .startOtp("startOtp1")
            .cancelReason("cancelReason1")
            .loyaltyPointsUsed(1)
            .rescheduleCount(1);
    }

    public static Booking getBookingSample2() {
        return new Booking()
            .id(2L)
            .bookingNo("bookingNo2")
            .publicId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .serviceTitle("serviceTitle2")
            .currency("currency2")
            .customerNotes("customerNotes2")
            .startOtp("startOtp2")
            .cancelReason("cancelReason2")
            .loyaltyPointsUsed(2)
            .rescheduleCount(2);
    }

    public static Booking getBookingRandomSampleGenerator() {
        return new Booking()
            .id(longCount.incrementAndGet())
            .bookingNo(UUID.randomUUID().toString())
            .publicId(UUID.randomUUID())
            .serviceTitle(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .customerNotes(UUID.randomUUID().toString())
            .startOtp(UUID.randomUUID().toString())
            .cancelReason(UUID.randomUUID().toString())
            .loyaltyPointsUsed(intCount.incrementAndGet())
            .rescheduleCount(intCount.incrementAndGet());
    }
}

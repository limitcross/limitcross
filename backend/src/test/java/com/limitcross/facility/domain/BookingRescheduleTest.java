package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingRescheduleTestSamples.*;
import static com.limitcross.facility.domain.BookingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingRescheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingReschedule.class);
        BookingReschedule bookingReschedule1 = getBookingRescheduleSample1();
        BookingReschedule bookingReschedule2 = new BookingReschedule();
        assertThat(bookingReschedule1).isNotEqualTo(bookingReschedule2);

        bookingReschedule2.setId(bookingReschedule1.getId());
        assertThat(bookingReschedule1).isEqualTo(bookingReschedule2);

        bookingReschedule2 = getBookingRescheduleSample2();
        assertThat(bookingReschedule1).isNotEqualTo(bookingReschedule2);
    }

    @Test
    void bookingTest() {
        BookingReschedule bookingReschedule = getBookingRescheduleRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        bookingReschedule.setBooking(bookingBack);
        assertThat(bookingReschedule.getBooking()).isEqualTo(bookingBack);

        bookingReschedule.booking(null);
        assertThat(bookingReschedule.getBooking()).isNull();
    }
}

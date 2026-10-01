package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingStatusHistoryTestSamples.*;
import static com.limitcross.facility.domain.BookingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingStatusHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingStatusHistory.class);
        BookingStatusHistory bookingStatusHistory1 = getBookingStatusHistorySample1();
        BookingStatusHistory bookingStatusHistory2 = new BookingStatusHistory();
        assertThat(bookingStatusHistory1).isNotEqualTo(bookingStatusHistory2);

        bookingStatusHistory2.setId(bookingStatusHistory1.getId());
        assertThat(bookingStatusHistory1).isEqualTo(bookingStatusHistory2);

        bookingStatusHistory2 = getBookingStatusHistorySample2();
        assertThat(bookingStatusHistory1).isNotEqualTo(bookingStatusHistory2);
    }

    @Test
    void bookingTest() {
        BookingStatusHistory bookingStatusHistory = getBookingStatusHistoryRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        bookingStatusHistory.setBooking(bookingBack);
        assertThat(bookingStatusHistory.getBooking()).isEqualTo(bookingBack);

        bookingStatusHistory.booking(null);
        assertThat(bookingStatusHistory.getBooking()).isNull();
    }
}

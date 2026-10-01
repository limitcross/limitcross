package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingMediaTestSamples.*;
import static com.limitcross.facility.domain.BookingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingMedia.class);
        BookingMedia bookingMedia1 = getBookingMediaSample1();
        BookingMedia bookingMedia2 = new BookingMedia();
        assertThat(bookingMedia1).isNotEqualTo(bookingMedia2);

        bookingMedia2.setId(bookingMedia1.getId());
        assertThat(bookingMedia1).isEqualTo(bookingMedia2);

        bookingMedia2 = getBookingMediaSample2();
        assertThat(bookingMedia1).isNotEqualTo(bookingMedia2);
    }

    @Test
    void bookingTest() {
        BookingMedia bookingMedia = getBookingMediaRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        bookingMedia.setBooking(bookingBack);
        assertThat(bookingMedia.getBooking()).isEqualTo(bookingBack);

        bookingMedia.booking(null);
        assertThat(bookingMedia.getBooking()).isNull();
    }
}

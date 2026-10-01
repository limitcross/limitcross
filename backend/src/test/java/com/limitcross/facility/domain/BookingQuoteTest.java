package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingQuoteItemTestSamples.*;
import static com.limitcross.facility.domain.BookingQuoteTestSamples.*;
import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookingQuoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingQuote.class);
        BookingQuote bookingQuote1 = getBookingQuoteSample1();
        BookingQuote bookingQuote2 = new BookingQuote();
        assertThat(bookingQuote1).isNotEqualTo(bookingQuote2);

        bookingQuote2.setId(bookingQuote1.getId());
        assertThat(bookingQuote1).isEqualTo(bookingQuote2);

        bookingQuote2 = getBookingQuoteSample2();
        assertThat(bookingQuote1).isNotEqualTo(bookingQuote2);
    }

    @Test
    void itemTest() {
        BookingQuote bookingQuote = getBookingQuoteRandomSampleGenerator();
        BookingQuoteItem bookingQuoteItemBack = getBookingQuoteItemRandomSampleGenerator();

        bookingQuote.addItem(bookingQuoteItemBack);
        assertThat(bookingQuote.getItems()).containsOnly(bookingQuoteItemBack);
        assertThat(bookingQuoteItemBack.getQuote()).isEqualTo(bookingQuote);

        bookingQuote.removeItem(bookingQuoteItemBack);
        assertThat(bookingQuote.getItems()).doesNotContain(bookingQuoteItemBack);
        assertThat(bookingQuoteItemBack.getQuote()).isNull();

        bookingQuote.items(new HashSet<>(Set.of(bookingQuoteItemBack)));
        assertThat(bookingQuote.getItems()).containsOnly(bookingQuoteItemBack);
        assertThat(bookingQuoteItemBack.getQuote()).isEqualTo(bookingQuote);

        bookingQuote.setItems(new HashSet<>());
        assertThat(bookingQuote.getItems()).doesNotContain(bookingQuoteItemBack);
        assertThat(bookingQuoteItemBack.getQuote()).isNull();
    }

    @Test
    void professionalTest() {
        BookingQuote bookingQuote = getBookingQuoteRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        bookingQuote.setProfessional(professionalBack);
        assertThat(bookingQuote.getProfessional()).isEqualTo(professionalBack);

        bookingQuote.professional(null);
        assertThat(bookingQuote.getProfessional()).isNull();
    }

    @Test
    void bookingTest() {
        BookingQuote bookingQuote = getBookingQuoteRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        bookingQuote.setBooking(bookingBack);
        assertThat(bookingQuote.getBooking()).isEqualTo(bookingBack);

        bookingQuote.booking(null);
        assertThat(bookingQuote.getBooking()).isNull();
    }
}

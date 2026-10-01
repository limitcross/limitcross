package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingQuoteItemTestSamples.*;
import static com.limitcross.facility.domain.BookingQuoteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingQuoteItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingQuoteItem.class);
        BookingQuoteItem bookingQuoteItem1 = getBookingQuoteItemSample1();
        BookingQuoteItem bookingQuoteItem2 = new BookingQuoteItem();
        assertThat(bookingQuoteItem1).isNotEqualTo(bookingQuoteItem2);

        bookingQuoteItem2.setId(bookingQuoteItem1.getId());
        assertThat(bookingQuoteItem1).isEqualTo(bookingQuoteItem2);

        bookingQuoteItem2 = getBookingQuoteItemSample2();
        assertThat(bookingQuoteItem1).isNotEqualTo(bookingQuoteItem2);
    }

    @Test
    void quoteTest() {
        BookingQuoteItem bookingQuoteItem = getBookingQuoteItemRandomSampleGenerator();
        BookingQuote bookingQuoteBack = getBookingQuoteRandomSampleGenerator();

        bookingQuoteItem.setQuote(bookingQuoteBack);
        assertThat(bookingQuoteItem.getQuote()).isEqualTo(bookingQuoteBack);

        bookingQuoteItem.quote(null);
        assertThat(bookingQuoteItem.getQuote()).isNull();
    }
}

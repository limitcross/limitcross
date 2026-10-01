package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.SupportTicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupportTicketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupportTicket.class);
        SupportTicket supportTicket1 = getSupportTicketSample1();
        SupportTicket supportTicket2 = new SupportTicket();
        assertThat(supportTicket1).isNotEqualTo(supportTicket2);

        supportTicket2.setId(supportTicket1.getId());
        assertThat(supportTicket1).isEqualTo(supportTicket2);

        supportTicket2 = getSupportTicketSample2();
        assertThat(supportTicket1).isNotEqualTo(supportTicket2);
    }

    @Test
    void bookingTest() {
        SupportTicket supportTicket = getSupportTicketRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        supportTicket.setBooking(bookingBack);
        assertThat(supportTicket.getBooking()).isEqualTo(bookingBack);

        supportTicket.booking(null);
        assertThat(supportTicket.getBooking()).isNull();
    }
}

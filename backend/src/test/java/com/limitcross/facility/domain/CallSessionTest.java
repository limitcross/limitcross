package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.CallSessionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CallSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CallSession.class);
        CallSession callSession1 = getCallSessionSample1();
        CallSession callSession2 = new CallSession();
        assertThat(callSession1).isNotEqualTo(callSession2);

        callSession2.setId(callSession1.getId());
        assertThat(callSession1).isEqualTo(callSession2);

        callSession2 = getCallSessionSample2();
        assertThat(callSession1).isNotEqualTo(callSession2);
    }

    @Test
    void bookingTest() {
        CallSession callSession = getCallSessionRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        callSession.setBooking(bookingBack);
        assertThat(callSession.getBooking()).isEqualTo(bookingBack);

        callSession.booking(null);
        assertThat(callSession.getBooking()).isNull();
    }
}

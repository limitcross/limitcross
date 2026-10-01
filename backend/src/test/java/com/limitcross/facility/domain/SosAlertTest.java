package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.SosAlertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SosAlertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SosAlert.class);
        SosAlert sosAlert1 = getSosAlertSample1();
        SosAlert sosAlert2 = new SosAlert();
        assertThat(sosAlert1).isNotEqualTo(sosAlert2);

        sosAlert2.setId(sosAlert1.getId());
        assertThat(sosAlert1).isEqualTo(sosAlert2);

        sosAlert2 = getSosAlertSample2();
        assertThat(sosAlert1).isNotEqualTo(sosAlert2);
    }

    @Test
    void bookingTest() {
        SosAlert sosAlert = getSosAlertRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        sosAlert.setBooking(bookingBack);
        assertThat(sosAlert.getBooking()).isEqualTo(bookingBack);

        sosAlert.booking(null);
        assertThat(sosAlert.getBooking()).isNull();
    }
}

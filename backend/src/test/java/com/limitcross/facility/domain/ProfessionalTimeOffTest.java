package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTimeOffTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTimeOffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalTimeOff.class);
        ProfessionalTimeOff professionalTimeOff1 = getProfessionalTimeOffSample1();
        ProfessionalTimeOff professionalTimeOff2 = new ProfessionalTimeOff();
        assertThat(professionalTimeOff1).isNotEqualTo(professionalTimeOff2);

        professionalTimeOff2.setId(professionalTimeOff1.getId());
        assertThat(professionalTimeOff1).isEqualTo(professionalTimeOff2);

        professionalTimeOff2 = getProfessionalTimeOffSample2();
        assertThat(professionalTimeOff1).isNotEqualTo(professionalTimeOff2);
    }

    @Test
    void bookingTest() {
        ProfessionalTimeOff professionalTimeOff = getProfessionalTimeOffRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        professionalTimeOff.setBooking(bookingBack);
        assertThat(professionalTimeOff.getBooking()).isEqualTo(bookingBack);

        professionalTimeOff.booking(null);
        assertThat(professionalTimeOff.getBooking()).isNull();
    }

    @Test
    void professionalTest() {
        ProfessionalTimeOff professionalTimeOff = getProfessionalTimeOffRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalTimeOff.setProfessional(professionalBack);
        assertThat(professionalTimeOff.getProfessional()).isEqualTo(professionalBack);

        professionalTimeOff.professional(null);
        assertThat(professionalTimeOff.getProfessional()).isNull();
    }
}

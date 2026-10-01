package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingAssignmentTestSamples.*;
import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingAssignment.class);
        BookingAssignment bookingAssignment1 = getBookingAssignmentSample1();
        BookingAssignment bookingAssignment2 = new BookingAssignment();
        assertThat(bookingAssignment1).isNotEqualTo(bookingAssignment2);

        bookingAssignment2.setId(bookingAssignment1.getId());
        assertThat(bookingAssignment1).isEqualTo(bookingAssignment2);

        bookingAssignment2 = getBookingAssignmentSample2();
        assertThat(bookingAssignment1).isNotEqualTo(bookingAssignment2);
    }

    @Test
    void professionalTest() {
        BookingAssignment bookingAssignment = getBookingAssignmentRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        bookingAssignment.setProfessional(professionalBack);
        assertThat(bookingAssignment.getProfessional()).isEqualTo(professionalBack);

        bookingAssignment.professional(null);
        assertThat(bookingAssignment.getProfessional()).isNull();
    }

    @Test
    void bookingTest() {
        BookingAssignment bookingAssignment = getBookingAssignmentRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        bookingAssignment.setBooking(bookingBack);
        assertThat(bookingAssignment.getBooking()).isEqualTo(bookingBack);

        bookingAssignment.booking(null);
        assertThat(bookingAssignment.getBooking()).isNull();
    }
}

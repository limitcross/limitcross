package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ReviewTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = getReviewSample1();
        Review review2 = new Review();
        assertThat(review1).isNotEqualTo(review2);

        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);

        review2 = getReviewSample2();
        assertThat(review1).isNotEqualTo(review2);
    }

    @Test
    void bookingTest() {
        Review review = getReviewRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        review.setBooking(bookingBack);
        assertThat(review.getBooking()).isEqualTo(bookingBack);

        review.booking(null);
        assertThat(review.getBooking()).isNull();
    }

    @Test
    void professionalTest() {
        Review review = getReviewRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        review.setProfessional(professionalBack);
        assertThat(review.getProfessional()).isEqualTo(professionalBack);

        review.professional(null);
        assertThat(review.getProfessional()).isNull();
    }

    @Test
    void serviceTest() {
        Review review = getReviewRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        review.setService(facilityServiceBack);
        assertThat(review.getService()).isEqualTo(facilityServiceBack);

        review.service(null);
        assertThat(review.getService()).isNull();
    }
}

package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.WarrantyClaimTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WarrantyClaimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarrantyClaim.class);
        WarrantyClaim warrantyClaim1 = getWarrantyClaimSample1();
        WarrantyClaim warrantyClaim2 = new WarrantyClaim();
        assertThat(warrantyClaim1).isNotEqualTo(warrantyClaim2);

        warrantyClaim2.setId(warrantyClaim1.getId());
        assertThat(warrantyClaim1).isEqualTo(warrantyClaim2);

        warrantyClaim2 = getWarrantyClaimSample2();
        assertThat(warrantyClaim1).isNotEqualTo(warrantyClaim2);
    }

    @Test
    void bookingTest() {
        WarrantyClaim warrantyClaim = getWarrantyClaimRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        warrantyClaim.setBooking(bookingBack);
        assertThat(warrantyClaim.getBooking()).isEqualTo(bookingBack);

        warrantyClaim.booking(null);
        assertThat(warrantyClaim.getBooking()).isNull();
    }

    @Test
    void redoBookingTest() {
        WarrantyClaim warrantyClaim = getWarrantyClaimRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        warrantyClaim.setRedoBooking(bookingBack);
        assertThat(warrantyClaim.getRedoBooking()).isEqualTo(bookingBack);

        warrantyClaim.redoBooking(null);
        assertThat(warrantyClaim.getRedoBooking()).isNull();
    }
}

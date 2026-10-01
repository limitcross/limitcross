package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.LoyaltyLedgerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoyaltyLedgerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoyaltyLedger.class);
        LoyaltyLedger loyaltyLedger1 = getLoyaltyLedgerSample1();
        LoyaltyLedger loyaltyLedger2 = new LoyaltyLedger();
        assertThat(loyaltyLedger1).isNotEqualTo(loyaltyLedger2);

        loyaltyLedger2.setId(loyaltyLedger1.getId());
        assertThat(loyaltyLedger1).isEqualTo(loyaltyLedger2);

        loyaltyLedger2 = getLoyaltyLedgerSample2();
        assertThat(loyaltyLedger1).isNotEqualTo(loyaltyLedger2);
    }

    @Test
    void bookingTest() {
        LoyaltyLedger loyaltyLedger = getLoyaltyLedgerRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        loyaltyLedger.setBooking(bookingBack);
        assertThat(loyaltyLedger.getBooking()).isEqualTo(bookingBack);

        loyaltyLedger.booking(null);
        assertThat(loyaltyLedger.getBooking()).isNull();
    }
}

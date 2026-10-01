package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WalletTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletTransaction.class);
        WalletTransaction walletTransaction1 = getWalletTransactionSample1();
        WalletTransaction walletTransaction2 = new WalletTransaction();
        assertThat(walletTransaction1).isNotEqualTo(walletTransaction2);

        walletTransaction2.setId(walletTransaction1.getId());
        assertThat(walletTransaction1).isEqualTo(walletTransaction2);

        walletTransaction2 = getWalletTransactionSample2();
        assertThat(walletTransaction1).isNotEqualTo(walletTransaction2);
    }

    @Test
    void professionalTest() {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        walletTransaction.setProfessional(professionalBack);
        assertThat(walletTransaction.getProfessional()).isEqualTo(professionalBack);

        walletTransaction.professional(null);
        assertThat(walletTransaction.getProfessional()).isNull();
    }

    @Test
    void bookingTest() {
        WalletTransaction walletTransaction = getWalletTransactionRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        walletTransaction.setBooking(bookingBack);
        assertThat(walletTransaction.getBooking()).isEqualTo(bookingBack);

        walletTransaction.booking(null);
        assertThat(walletTransaction.getBooking()).isNull();
    }
}

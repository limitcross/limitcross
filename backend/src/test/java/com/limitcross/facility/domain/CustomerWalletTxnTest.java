package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.CustomerWalletTxnTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerWalletTxnTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerWalletTxn.class);
        CustomerWalletTxn customerWalletTxn1 = getCustomerWalletTxnSample1();
        CustomerWalletTxn customerWalletTxn2 = new CustomerWalletTxn();
        assertThat(customerWalletTxn1).isNotEqualTo(customerWalletTxn2);

        customerWalletTxn2.setId(customerWalletTxn1.getId());
        assertThat(customerWalletTxn1).isEqualTo(customerWalletTxn2);

        customerWalletTxn2 = getCustomerWalletTxnSample2();
        assertThat(customerWalletTxn1).isNotEqualTo(customerWalletTxn2);
    }

    @Test
    void bookingTest() {
        CustomerWalletTxn customerWalletTxn = getCustomerWalletTxnRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        customerWalletTxn.setBooking(bookingBack);
        assertThat(customerWalletTxn.getBooking()).isEqualTo(bookingBack);

        customerWalletTxn.booking(null);
        assertThat(customerWalletTxn.getBooking()).isNull();
    }
}

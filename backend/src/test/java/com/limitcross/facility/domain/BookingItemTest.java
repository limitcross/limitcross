package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingItemTestSamples.*;
import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.ServiceAddonTestSamples.*;
import static com.limitcross.facility.domain.ServicePackageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingItem.class);
        BookingItem bookingItem1 = getBookingItemSample1();
        BookingItem bookingItem2 = new BookingItem();
        assertThat(bookingItem1).isNotEqualTo(bookingItem2);

        bookingItem2.setId(bookingItem1.getId());
        assertThat(bookingItem1).isEqualTo(bookingItem2);

        bookingItem2 = getBookingItemSample2();
        assertThat(bookingItem1).isNotEqualTo(bookingItem2);
    }

    @Test
    void servicePackageTest() {
        BookingItem bookingItem = getBookingItemRandomSampleGenerator();
        ServicePackage servicePackageBack = getServicePackageRandomSampleGenerator();

        bookingItem.setServicePackage(servicePackageBack);
        assertThat(bookingItem.getServicePackage()).isEqualTo(servicePackageBack);

        bookingItem.servicePackage(null);
        assertThat(bookingItem.getServicePackage()).isNull();
    }

    @Test
    void addonTest() {
        BookingItem bookingItem = getBookingItemRandomSampleGenerator();
        ServiceAddon serviceAddonBack = getServiceAddonRandomSampleGenerator();

        bookingItem.setAddon(serviceAddonBack);
        assertThat(bookingItem.getAddon()).isEqualTo(serviceAddonBack);

        bookingItem.addon(null);
        assertThat(bookingItem.getAddon()).isNull();
    }

    @Test
    void bookingTest() {
        BookingItem bookingItem = getBookingItemRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        bookingItem.setBooking(bookingBack);
        assertThat(bookingItem.getBooking()).isEqualTo(bookingBack);

        bookingItem.booking(null);
        assertThat(bookingItem.getBooking()).isNull();
    }
}

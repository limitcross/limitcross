package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingSubscriptionTestSamples.*;
import static com.limitcross.facility.domain.CustomerAddressTestSamples.*;
import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ServicePackageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingSubscription.class);
        BookingSubscription bookingSubscription1 = getBookingSubscriptionSample1();
        BookingSubscription bookingSubscription2 = new BookingSubscription();
        assertThat(bookingSubscription1).isNotEqualTo(bookingSubscription2);

        bookingSubscription2.setId(bookingSubscription1.getId());
        assertThat(bookingSubscription1).isEqualTo(bookingSubscription2);

        bookingSubscription2 = getBookingSubscriptionSample2();
        assertThat(bookingSubscription1).isNotEqualTo(bookingSubscription2);
    }

    @Test
    void serviceTest() {
        BookingSubscription bookingSubscription = getBookingSubscriptionRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        bookingSubscription.setService(facilityServiceBack);
        assertThat(bookingSubscription.getService()).isEqualTo(facilityServiceBack);

        bookingSubscription.service(null);
        assertThat(bookingSubscription.getService()).isNull();
    }

    @Test
    void servicePackageTest() {
        BookingSubscription bookingSubscription = getBookingSubscriptionRandomSampleGenerator();
        ServicePackage servicePackageBack = getServicePackageRandomSampleGenerator();

        bookingSubscription.setServicePackage(servicePackageBack);
        assertThat(bookingSubscription.getServicePackage()).isEqualTo(servicePackageBack);

        bookingSubscription.servicePackage(null);
        assertThat(bookingSubscription.getServicePackage()).isNull();
    }

    @Test
    void addressTest() {
        BookingSubscription bookingSubscription = getBookingSubscriptionRandomSampleGenerator();
        CustomerAddress customerAddressBack = getCustomerAddressRandomSampleGenerator();

        bookingSubscription.setAddress(customerAddressBack);
        assertThat(bookingSubscription.getAddress()).isEqualTo(customerAddressBack);

        bookingSubscription.address(null);
        assertThat(bookingSubscription.getAddress()).isNull();
    }

    @Test
    void preferredProfessionalTest() {
        BookingSubscription bookingSubscription = getBookingSubscriptionRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        bookingSubscription.setPreferredProfessional(professionalBack);
        assertThat(bookingSubscription.getPreferredProfessional()).isEqualTo(professionalBack);

        bookingSubscription.preferredProfessional(null);
        assertThat(bookingSubscription.getPreferredProfessional()).isNull();
    }
}

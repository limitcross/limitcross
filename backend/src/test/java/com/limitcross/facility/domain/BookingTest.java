package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingAssignmentTestSamples.*;
import static com.limitcross.facility.domain.BookingItemTestSamples.*;
import static com.limitcross.facility.domain.BookingMediaTestSamples.*;
import static com.limitcross.facility.domain.BookingQuoteTestSamples.*;
import static com.limitcross.facility.domain.BookingRescheduleTestSamples.*;
import static com.limitcross.facility.domain.BookingStatusHistoryTestSamples.*;
import static com.limitcross.facility.domain.BookingSubscriptionTestSamples.*;
import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.ChatThreadTestSamples.*;
import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.CouponRedemptionTestSamples.*;
import static com.limitcross.facility.domain.CouponTestSamples.*;
import static com.limitcross.facility.domain.CustomerAddressTestSamples.*;
import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.PaymentTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ReviewTestSamples.*;
import static com.limitcross.facility.domain.SlotCapacityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = getBookingSample1();
        Booking booking2 = new Booking();
        assertThat(booking1).isNotEqualTo(booking2);

        booking2.setId(booking1.getId());
        assertThat(booking1).isEqualTo(booking2);

        booking2 = getBookingSample2();
        assertThat(booking1).isNotEqualTo(booking2);
    }

    @Test
    void itemTest() {
        Booking booking = getBookingRandomSampleGenerator();
        BookingItem bookingItemBack = getBookingItemRandomSampleGenerator();

        booking.addItem(bookingItemBack);
        assertThat(booking.getItems()).containsOnly(bookingItemBack);
        assertThat(bookingItemBack.getBooking()).isEqualTo(booking);

        booking.removeItem(bookingItemBack);
        assertThat(booking.getItems()).doesNotContain(bookingItemBack);
        assertThat(bookingItemBack.getBooking()).isNull();

        booking.items(new HashSet<>(Set.of(bookingItemBack)));
        assertThat(booking.getItems()).containsOnly(bookingItemBack);
        assertThat(bookingItemBack.getBooking()).isEqualTo(booking);

        booking.setItems(new HashSet<>());
        assertThat(booking.getItems()).doesNotContain(bookingItemBack);
        assertThat(bookingItemBack.getBooking()).isNull();
    }

    @Test
    void statusHistoryTest() {
        Booking booking = getBookingRandomSampleGenerator();
        BookingStatusHistory bookingStatusHistoryBack = getBookingStatusHistoryRandomSampleGenerator();

        booking.addStatusHistory(bookingStatusHistoryBack);
        assertThat(booking.getStatusHistories()).containsOnly(bookingStatusHistoryBack);
        assertThat(bookingStatusHistoryBack.getBooking()).isEqualTo(booking);

        booking.removeStatusHistory(bookingStatusHistoryBack);
        assertThat(booking.getStatusHistories()).doesNotContain(bookingStatusHistoryBack);
        assertThat(bookingStatusHistoryBack.getBooking()).isNull();

        booking.statusHistories(new HashSet<>(Set.of(bookingStatusHistoryBack)));
        assertThat(booking.getStatusHistories()).containsOnly(bookingStatusHistoryBack);
        assertThat(bookingStatusHistoryBack.getBooking()).isEqualTo(booking);

        booking.setStatusHistories(new HashSet<>());
        assertThat(booking.getStatusHistories()).doesNotContain(bookingStatusHistoryBack);
        assertThat(bookingStatusHistoryBack.getBooking()).isNull();
    }

    @Test
    void assignmentTest() {
        Booking booking = getBookingRandomSampleGenerator();
        BookingAssignment bookingAssignmentBack = getBookingAssignmentRandomSampleGenerator();

        booking.addAssignment(bookingAssignmentBack);
        assertThat(booking.getAssignments()).containsOnly(bookingAssignmentBack);
        assertThat(bookingAssignmentBack.getBooking()).isEqualTo(booking);

        booking.removeAssignment(bookingAssignmentBack);
        assertThat(booking.getAssignments()).doesNotContain(bookingAssignmentBack);
        assertThat(bookingAssignmentBack.getBooking()).isNull();

        booking.assignments(new HashSet<>(Set.of(bookingAssignmentBack)));
        assertThat(booking.getAssignments()).containsOnly(bookingAssignmentBack);
        assertThat(bookingAssignmentBack.getBooking()).isEqualTo(booking);

        booking.setAssignments(new HashSet<>());
        assertThat(booking.getAssignments()).doesNotContain(bookingAssignmentBack);
        assertThat(bookingAssignmentBack.getBooking()).isNull();
    }

    @Test
    void mediaTest() {
        Booking booking = getBookingRandomSampleGenerator();
        BookingMedia bookingMediaBack = getBookingMediaRandomSampleGenerator();

        booking.addMedia(bookingMediaBack);
        assertThat(booking.getMedia()).containsOnly(bookingMediaBack);
        assertThat(bookingMediaBack.getBooking()).isEqualTo(booking);

        booking.removeMedia(bookingMediaBack);
        assertThat(booking.getMedia()).doesNotContain(bookingMediaBack);
        assertThat(bookingMediaBack.getBooking()).isNull();

        booking.media(new HashSet<>(Set.of(bookingMediaBack)));
        assertThat(booking.getMedia()).containsOnly(bookingMediaBack);
        assertThat(bookingMediaBack.getBooking()).isEqualTo(booking);

        booking.setMedia(new HashSet<>());
        assertThat(booking.getMedia()).doesNotContain(bookingMediaBack);
        assertThat(bookingMediaBack.getBooking()).isNull();
    }

    @Test
    void paymentTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        booking.addPayment(paymentBack);
        assertThat(booking.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getBooking()).isEqualTo(booking);

        booking.removePayment(paymentBack);
        assertThat(booking.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getBooking()).isNull();

        booking.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(booking.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getBooking()).isEqualTo(booking);

        booking.setPayments(new HashSet<>());
        assertThat(booking.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getBooking()).isNull();
    }

    @Test
    void quoteTest() {
        Booking booking = getBookingRandomSampleGenerator();
        BookingQuote bookingQuoteBack = getBookingQuoteRandomSampleGenerator();

        booking.addQuote(bookingQuoteBack);
        assertThat(booking.getQuotes()).containsOnly(bookingQuoteBack);
        assertThat(bookingQuoteBack.getBooking()).isEqualTo(booking);

        booking.removeQuote(bookingQuoteBack);
        assertThat(booking.getQuotes()).doesNotContain(bookingQuoteBack);
        assertThat(bookingQuoteBack.getBooking()).isNull();

        booking.quotes(new HashSet<>(Set.of(bookingQuoteBack)));
        assertThat(booking.getQuotes()).containsOnly(bookingQuoteBack);
        assertThat(bookingQuoteBack.getBooking()).isEqualTo(booking);

        booking.setQuotes(new HashSet<>());
        assertThat(booking.getQuotes()).doesNotContain(bookingQuoteBack);
        assertThat(bookingQuoteBack.getBooking()).isNull();
    }

    @Test
    void rescheduleTest() {
        Booking booking = getBookingRandomSampleGenerator();
        BookingReschedule bookingRescheduleBack = getBookingRescheduleRandomSampleGenerator();

        booking.addReschedule(bookingRescheduleBack);
        assertThat(booking.getReschedules()).containsOnly(bookingRescheduleBack);
        assertThat(bookingRescheduleBack.getBooking()).isEqualTo(booking);

        booking.removeReschedule(bookingRescheduleBack);
        assertThat(booking.getReschedules()).doesNotContain(bookingRescheduleBack);
        assertThat(bookingRescheduleBack.getBooking()).isNull();

        booking.reschedules(new HashSet<>(Set.of(bookingRescheduleBack)));
        assertThat(booking.getReschedules()).containsOnly(bookingRescheduleBack);
        assertThat(bookingRescheduleBack.getBooking()).isEqualTo(booking);

        booking.setReschedules(new HashSet<>());
        assertThat(booking.getReschedules()).doesNotContain(bookingRescheduleBack);
        assertThat(bookingRescheduleBack.getBooking()).isNull();
    }

    @Test
    void serviceTest() {
        Booking booking = getBookingRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        booking.setService(facilityServiceBack);
        assertThat(booking.getService()).isEqualTo(facilityServiceBack);

        booking.service(null);
        assertThat(booking.getService()).isNull();
    }

    @Test
    void cityTest() {
        Booking booking = getBookingRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        booking.setCity(cityBack);
        assertThat(booking.getCity()).isEqualTo(cityBack);

        booking.city(null);
        assertThat(booking.getCity()).isNull();
    }

    @Test
    void addressTest() {
        Booking booking = getBookingRandomSampleGenerator();
        CustomerAddress customerAddressBack = getCustomerAddressRandomSampleGenerator();

        booking.setAddress(customerAddressBack);
        assertThat(booking.getAddress()).isEqualTo(customerAddressBack);

        booking.address(null);
        assertThat(booking.getAddress()).isNull();
    }

    @Test
    void professionalTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        booking.setProfessional(professionalBack);
        assertThat(booking.getProfessional()).isEqualTo(professionalBack);

        booking.professional(null);
        assertThat(booking.getProfessional()).isNull();
    }

    @Test
    void couponTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Coupon couponBack = getCouponRandomSampleGenerator();

        booking.setCoupon(couponBack);
        assertThat(booking.getCoupon()).isEqualTo(couponBack);

        booking.coupon(null);
        assertThat(booking.getCoupon()).isNull();
    }

    @Test
    void subscriptionTest() {
        Booking booking = getBookingRandomSampleGenerator();
        BookingSubscription bookingSubscriptionBack = getBookingSubscriptionRandomSampleGenerator();

        booking.setSubscription(bookingSubscriptionBack);
        assertThat(booking.getSubscription()).isEqualTo(bookingSubscriptionBack);

        booking.subscription(null);
        assertThat(booking.getSubscription()).isNull();
    }

    @Test
    void slotCapacityTest() {
        Booking booking = getBookingRandomSampleGenerator();
        SlotCapacity slotCapacityBack = getSlotCapacityRandomSampleGenerator();

        booking.setSlotCapacity(slotCapacityBack);
        assertThat(booking.getSlotCapacity()).isEqualTo(slotCapacityBack);

        booking.slotCapacity(null);
        assertThat(booking.getSlotCapacity()).isNull();
    }

    @Test
    void couponRedemptionTest() {
        Booking booking = getBookingRandomSampleGenerator();
        CouponRedemption couponRedemptionBack = getCouponRedemptionRandomSampleGenerator();

        booking.setCouponRedemption(couponRedemptionBack);
        assertThat(booking.getCouponRedemption()).isEqualTo(couponRedemptionBack);
        assertThat(couponRedemptionBack.getBooking()).isEqualTo(booking);

        booking.couponRedemption(null);
        assertThat(booking.getCouponRedemption()).isNull();
        assertThat(couponRedemptionBack.getBooking()).isNull();
    }

    @Test
    void reviewTest() {
        Booking booking = getBookingRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        booking.setReview(reviewBack);
        assertThat(booking.getReview()).isEqualTo(reviewBack);
        assertThat(reviewBack.getBooking()).isEqualTo(booking);

        booking.review(null);
        assertThat(booking.getReview()).isNull();
        assertThat(reviewBack.getBooking()).isNull();
    }

    @Test
    void chatThreadTest() {
        Booking booking = getBookingRandomSampleGenerator();
        ChatThread chatThreadBack = getChatThreadRandomSampleGenerator();

        booking.setChatThread(chatThreadBack);
        assertThat(booking.getChatThread()).isEqualTo(chatThreadBack);
        assertThat(chatThreadBack.getBooking()).isEqualTo(booking);

        booking.chatThread(null);
        assertThat(booking.getChatThread()).isNull();
        assertThat(chatThreadBack.getBooking()).isNull();
    }
}

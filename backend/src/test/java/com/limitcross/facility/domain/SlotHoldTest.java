package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.SlotCapacityTestSamples.*;
import static com.limitcross.facility.domain.SlotHoldTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlotHoldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotHold.class);
        SlotHold slotHold1 = getSlotHoldSample1();
        SlotHold slotHold2 = new SlotHold();
        assertThat(slotHold1).isNotEqualTo(slotHold2);

        slotHold2.setId(slotHold1.getId());
        assertThat(slotHold1).isEqualTo(slotHold2);

        slotHold2 = getSlotHoldSample2();
        assertThat(slotHold1).isNotEqualTo(slotHold2);
    }

    @Test
    void slotCapacityTest() {
        SlotHold slotHold = getSlotHoldRandomSampleGenerator();
        SlotCapacity slotCapacityBack = getSlotCapacityRandomSampleGenerator();

        slotHold.setSlotCapacity(slotCapacityBack);
        assertThat(slotHold.getSlotCapacity()).isEqualTo(slotCapacityBack);

        slotHold.slotCapacity(null);
        assertThat(slotHold.getSlotCapacity()).isNull();
    }

    @Test
    void bookingTest() {
        SlotHold slotHold = getSlotHoldRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        slotHold.setBooking(bookingBack);
        assertThat(slotHold.getBooking()).isEqualTo(bookingBack);

        slotHold.booking(null);
        assertThat(slotHold.getBooking()).isNull();
    }
}

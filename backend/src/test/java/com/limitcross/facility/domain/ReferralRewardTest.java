package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BookingTestSamples.*;
import static com.limitcross.facility.domain.ReferralRewardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferralRewardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferralReward.class);
        ReferralReward referralReward1 = getReferralRewardSample1();
        ReferralReward referralReward2 = new ReferralReward();
        assertThat(referralReward1).isNotEqualTo(referralReward2);

        referralReward2.setId(referralReward1.getId());
        assertThat(referralReward1).isEqualTo(referralReward2);

        referralReward2 = getReferralRewardSample2();
        assertThat(referralReward1).isNotEqualTo(referralReward2);
    }

    @Test
    void triggerBookingTest() {
        ReferralReward referralReward = getReferralRewardRandomSampleGenerator();
        Booking bookingBack = getBookingRandomSampleGenerator();

        referralReward.setTriggerBooking(bookingBack);
        assertThat(referralReward.getTriggerBooking()).isEqualTo(bookingBack);

        referralReward.triggerBooking(null);
        assertThat(referralReward.getTriggerBooking()).isNull();
    }
}

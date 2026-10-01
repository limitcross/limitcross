package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.PayoutTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PayoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payout.class);
        Payout payout1 = getPayoutSample1();
        Payout payout2 = new Payout();
        assertThat(payout1).isNotEqualTo(payout2);

        payout2.setId(payout1.getId());
        assertThat(payout1).isEqualTo(payout2);

        payout2 = getPayoutSample2();
        assertThat(payout1).isNotEqualTo(payout2);
    }

    @Test
    void professionalTest() {
        Payout payout = getPayoutRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        payout.setProfessional(professionalBack);
        assertThat(payout.getProfessional()).isEqualTo(professionalBack);

        payout.professional(null);
        assertThat(payout.getProfessional()).isNull();
    }
}

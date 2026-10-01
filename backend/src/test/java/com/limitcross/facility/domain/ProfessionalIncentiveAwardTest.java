package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.IncentiveRuleTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalIncentiveAwardTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.WalletTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalIncentiveAwardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalIncentiveAward.class);
        ProfessionalIncentiveAward professionalIncentiveAward1 = getProfessionalIncentiveAwardSample1();
        ProfessionalIncentiveAward professionalIncentiveAward2 = new ProfessionalIncentiveAward();
        assertThat(professionalIncentiveAward1).isNotEqualTo(professionalIncentiveAward2);

        professionalIncentiveAward2.setId(professionalIncentiveAward1.getId());
        assertThat(professionalIncentiveAward1).isEqualTo(professionalIncentiveAward2);

        professionalIncentiveAward2 = getProfessionalIncentiveAwardSample2();
        assertThat(professionalIncentiveAward1).isNotEqualTo(professionalIncentiveAward2);
    }

    @Test
    void professionalTest() {
        ProfessionalIncentiveAward professionalIncentiveAward = getProfessionalIncentiveAwardRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalIncentiveAward.setProfessional(professionalBack);
        assertThat(professionalIncentiveAward.getProfessional()).isEqualTo(professionalBack);

        professionalIncentiveAward.professional(null);
        assertThat(professionalIncentiveAward.getProfessional()).isNull();
    }

    @Test
    void ruleTest() {
        ProfessionalIncentiveAward professionalIncentiveAward = getProfessionalIncentiveAwardRandomSampleGenerator();
        IncentiveRule incentiveRuleBack = getIncentiveRuleRandomSampleGenerator();

        professionalIncentiveAward.setRule(incentiveRuleBack);
        assertThat(professionalIncentiveAward.getRule()).isEqualTo(incentiveRuleBack);

        professionalIncentiveAward.rule(null);
        assertThat(professionalIncentiveAward.getRule()).isNull();
    }

    @Test
    void walletTransactionTest() {
        ProfessionalIncentiveAward professionalIncentiveAward = getProfessionalIncentiveAwardRandomSampleGenerator();
        WalletTransaction walletTransactionBack = getWalletTransactionRandomSampleGenerator();

        professionalIncentiveAward.setWalletTransaction(walletTransactionBack);
        assertThat(professionalIncentiveAward.getWalletTransaction()).isEqualTo(walletTransactionBack);

        professionalIncentiveAward.walletTransaction(null);
        assertThat(professionalIncentiveAward.getWalletTransaction()).isNull();
    }
}

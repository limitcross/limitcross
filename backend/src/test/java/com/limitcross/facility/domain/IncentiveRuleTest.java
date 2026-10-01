package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.IncentiveRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncentiveRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncentiveRule.class);
        IncentiveRule incentiveRule1 = getIncentiveRuleSample1();
        IncentiveRule incentiveRule2 = new IncentiveRule();
        assertThat(incentiveRule1).isNotEqualTo(incentiveRule2);

        incentiveRule2.setId(incentiveRule1.getId());
        assertThat(incentiveRule1).isEqualTo(incentiveRule2);

        incentiveRule2 = getIncentiveRuleSample2();
        assertThat(incentiveRule1).isNotEqualTo(incentiveRule2);
    }

    @Test
    void cityTest() {
        IncentiveRule incentiveRule = getIncentiveRuleRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        incentiveRule.setCity(cityBack);
        assertThat(incentiveRule.getCity()).isEqualTo(cityBack);

        incentiveRule.city(null);
        assertThat(incentiveRule.getCity()).isNull();
    }
}

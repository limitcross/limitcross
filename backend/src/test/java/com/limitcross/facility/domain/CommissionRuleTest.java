package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.CityTestSamples.*;
import static com.limitcross.facility.domain.CommissionRuleTestSamples.*;
import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTierTestSamples.*;
import static com.limitcross.facility.domain.ServiceCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommissionRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommissionRule.class);
        CommissionRule commissionRule1 = getCommissionRuleSample1();
        CommissionRule commissionRule2 = new CommissionRule();
        assertThat(commissionRule1).isNotEqualTo(commissionRule2);

        commissionRule2.setId(commissionRule1.getId());
        assertThat(commissionRule1).isEqualTo(commissionRule2);

        commissionRule2 = getCommissionRuleSample2();
        assertThat(commissionRule1).isNotEqualTo(commissionRule2);
    }

    @Test
    void serviceTest() {
        CommissionRule commissionRule = getCommissionRuleRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        commissionRule.setService(facilityServiceBack);
        assertThat(commissionRule.getService()).isEqualTo(facilityServiceBack);

        commissionRule.service(null);
        assertThat(commissionRule.getService()).isNull();
    }

    @Test
    void categoryTest() {
        CommissionRule commissionRule = getCommissionRuleRandomSampleGenerator();
        ServiceCategory serviceCategoryBack = getServiceCategoryRandomSampleGenerator();

        commissionRule.setCategory(serviceCategoryBack);
        assertThat(commissionRule.getCategory()).isEqualTo(serviceCategoryBack);

        commissionRule.category(null);
        assertThat(commissionRule.getCategory()).isNull();
    }

    @Test
    void cityTest() {
        CommissionRule commissionRule = getCommissionRuleRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        commissionRule.setCity(cityBack);
        assertThat(commissionRule.getCity()).isEqualTo(cityBack);

        commissionRule.city(null);
        assertThat(commissionRule.getCity()).isNull();
    }

    @Test
    void tierTest() {
        CommissionRule commissionRule = getCommissionRuleRandomSampleGenerator();
        ProfessionalTier professionalTierBack = getProfessionalTierRandomSampleGenerator();

        commissionRule.setTier(professionalTierBack);
        assertThat(commissionRule.getTier()).isEqualTo(professionalTierBack);

        commissionRule.tier(null);
        assertThat(commissionRule.getTier()).isNull();
    }
}

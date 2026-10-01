package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTierHistoryTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTierHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalTierHistory.class);
        ProfessionalTierHistory professionalTierHistory1 = getProfessionalTierHistorySample1();
        ProfessionalTierHistory professionalTierHistory2 = new ProfessionalTierHistory();
        assertThat(professionalTierHistory1).isNotEqualTo(professionalTierHistory2);

        professionalTierHistory2.setId(professionalTierHistory1.getId());
        assertThat(professionalTierHistory1).isEqualTo(professionalTierHistory2);

        professionalTierHistory2 = getProfessionalTierHistorySample2();
        assertThat(professionalTierHistory1).isNotEqualTo(professionalTierHistory2);
    }

    @Test
    void professionalTest() {
        ProfessionalTierHistory professionalTierHistory = getProfessionalTierHistoryRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalTierHistory.setProfessional(professionalBack);
        assertThat(professionalTierHistory.getProfessional()).isEqualTo(professionalBack);

        professionalTierHistory.professional(null);
        assertThat(professionalTierHistory.getProfessional()).isNull();
    }

    @Test
    void tierTest() {
        ProfessionalTierHistory professionalTierHistory = getProfessionalTierHistoryRandomSampleGenerator();
        ProfessionalTier professionalTierBack = getProfessionalTierRandomSampleGenerator();

        professionalTierHistory.setTier(professionalTierBack);
        assertThat(professionalTierHistory.getTier()).isEqualTo(professionalTierBack);

        professionalTierHistory.tier(null);
        assertThat(professionalTierHistory.getTier()).isNull();
    }
}

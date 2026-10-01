package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ProfessionalTierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalTier.class);
        ProfessionalTier professionalTier1 = getProfessionalTierSample1();
        ProfessionalTier professionalTier2 = new ProfessionalTier();
        assertThat(professionalTier1).isNotEqualTo(professionalTier2);

        professionalTier2.setId(professionalTier1.getId());
        assertThat(professionalTier1).isEqualTo(professionalTier2);

        professionalTier2 = getProfessionalTierSample2();
        assertThat(professionalTier1).isNotEqualTo(professionalTier2);
    }
}

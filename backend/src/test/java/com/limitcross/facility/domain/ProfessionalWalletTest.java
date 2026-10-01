package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalWalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalWalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalWallet.class);
        ProfessionalWallet professionalWallet1 = getProfessionalWalletSample1();
        ProfessionalWallet professionalWallet2 = new ProfessionalWallet();
        assertThat(professionalWallet1).isNotEqualTo(professionalWallet2);

        professionalWallet2.setId(professionalWallet1.getId());
        assertThat(professionalWallet1).isEqualTo(professionalWallet2);

        professionalWallet2 = getProfessionalWalletSample2();
        assertThat(professionalWallet1).isNotEqualTo(professionalWallet2);
    }

    @Test
    void professionalTest() {
        ProfessionalWallet professionalWallet = getProfessionalWalletRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalWallet.setProfessional(professionalBack);
        assertThat(professionalWallet.getProfessional()).isEqualTo(professionalBack);

        professionalWallet.professional(null);
        assertThat(professionalWallet.getProfessional()).isNull();
    }
}

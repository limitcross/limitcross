package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ProfessionalKycDocumentTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalKycDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalKycDocument.class);
        ProfessionalKycDocument professionalKycDocument1 = getProfessionalKycDocumentSample1();
        ProfessionalKycDocument professionalKycDocument2 = new ProfessionalKycDocument();
        assertThat(professionalKycDocument1).isNotEqualTo(professionalKycDocument2);

        professionalKycDocument2.setId(professionalKycDocument1.getId());
        assertThat(professionalKycDocument1).isEqualTo(professionalKycDocument2);

        professionalKycDocument2 = getProfessionalKycDocumentSample2();
        assertThat(professionalKycDocument1).isNotEqualTo(professionalKycDocument2);
    }

    @Test
    void professionalTest() {
        ProfessionalKycDocument professionalKycDocument = getProfessionalKycDocumentRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalKycDocument.setProfessional(professionalBack);
        assertThat(professionalKycDocument.getProfessional()).isEqualTo(professionalBack);

        professionalKycDocument.professional(null);
        assertThat(professionalKycDocument.getProfessional()).isNull();
    }
}

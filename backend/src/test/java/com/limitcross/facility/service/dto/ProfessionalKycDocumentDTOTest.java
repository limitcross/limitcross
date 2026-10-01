package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalKycDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalKycDocumentDTO.class);
        ProfessionalKycDocumentDTO professionalKycDocumentDTO1 = new ProfessionalKycDocumentDTO();
        professionalKycDocumentDTO1.setId(1L);
        ProfessionalKycDocumentDTO professionalKycDocumentDTO2 = new ProfessionalKycDocumentDTO();
        assertThat(professionalKycDocumentDTO1).isNotEqualTo(professionalKycDocumentDTO2);
        professionalKycDocumentDTO2.setId(professionalKycDocumentDTO1.getId());
        assertThat(professionalKycDocumentDTO1).isEqualTo(professionalKycDocumentDTO2);
        professionalKycDocumentDTO2.setId(2L);
        assertThat(professionalKycDocumentDTO1).isNotEqualTo(professionalKycDocumentDTO2);
        professionalKycDocumentDTO1.setId(null);
        assertThat(professionalKycDocumentDTO1).isNotEqualTo(professionalKycDocumentDTO2);
    }
}

package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalWalletDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalWalletDTO.class);
        ProfessionalWalletDTO professionalWalletDTO1 = new ProfessionalWalletDTO();
        professionalWalletDTO1.setId(1L);
        ProfessionalWalletDTO professionalWalletDTO2 = new ProfessionalWalletDTO();
        assertThat(professionalWalletDTO1).isNotEqualTo(professionalWalletDTO2);
        professionalWalletDTO2.setId(professionalWalletDTO1.getId());
        assertThat(professionalWalletDTO1).isEqualTo(professionalWalletDTO2);
        professionalWalletDTO2.setId(2L);
        assertThat(professionalWalletDTO1).isNotEqualTo(professionalWalletDTO2);
        professionalWalletDTO1.setId(null);
        assertThat(professionalWalletDTO1).isNotEqualTo(professionalWalletDTO2);
    }
}

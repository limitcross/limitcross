package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTierHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalTierHistoryDTO.class);
        ProfessionalTierHistoryDTO professionalTierHistoryDTO1 = new ProfessionalTierHistoryDTO();
        professionalTierHistoryDTO1.setId(1L);
        ProfessionalTierHistoryDTO professionalTierHistoryDTO2 = new ProfessionalTierHistoryDTO();
        assertThat(professionalTierHistoryDTO1).isNotEqualTo(professionalTierHistoryDTO2);
        professionalTierHistoryDTO2.setId(professionalTierHistoryDTO1.getId());
        assertThat(professionalTierHistoryDTO1).isEqualTo(professionalTierHistoryDTO2);
        professionalTierHistoryDTO2.setId(2L);
        assertThat(professionalTierHistoryDTO1).isNotEqualTo(professionalTierHistoryDTO2);
        professionalTierHistoryDTO1.setId(null);
        assertThat(professionalTierHistoryDTO1).isNotEqualTo(professionalTierHistoryDTO2);
    }
}

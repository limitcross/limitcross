package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalAvailabilityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalAvailabilityDTO.class);
        ProfessionalAvailabilityDTO professionalAvailabilityDTO1 = new ProfessionalAvailabilityDTO();
        professionalAvailabilityDTO1.setId(1L);
        ProfessionalAvailabilityDTO professionalAvailabilityDTO2 = new ProfessionalAvailabilityDTO();
        assertThat(professionalAvailabilityDTO1).isNotEqualTo(professionalAvailabilityDTO2);
        professionalAvailabilityDTO2.setId(professionalAvailabilityDTO1.getId());
        assertThat(professionalAvailabilityDTO1).isEqualTo(professionalAvailabilityDTO2);
        professionalAvailabilityDTO2.setId(2L);
        assertThat(professionalAvailabilityDTO1).isNotEqualTo(professionalAvailabilityDTO2);
        professionalAvailabilityDTO1.setId(null);
        assertThat(professionalAvailabilityDTO1).isNotEqualTo(professionalAvailabilityDTO2);
    }
}

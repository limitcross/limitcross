package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilityServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacilityServiceDTO.class);
        FacilityServiceDTO facilityServiceDTO1 = new FacilityServiceDTO();
        facilityServiceDTO1.setId(1L);
        FacilityServiceDTO facilityServiceDTO2 = new FacilityServiceDTO();
        assertThat(facilityServiceDTO1).isNotEqualTo(facilityServiceDTO2);
        facilityServiceDTO2.setId(facilityServiceDTO1.getId());
        assertThat(facilityServiceDTO1).isEqualTo(facilityServiceDTO2);
        facilityServiceDTO2.setId(2L);
        assertThat(facilityServiceDTO1).isNotEqualTo(facilityServiceDTO2);
        facilityServiceDTO1.setId(null);
        assertThat(facilityServiceDTO1).isNotEqualTo(facilityServiceDTO2);
    }
}

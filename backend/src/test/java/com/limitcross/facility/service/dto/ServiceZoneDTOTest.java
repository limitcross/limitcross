package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceZoneDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceZoneDTO.class);
        ServiceZoneDTO serviceZoneDTO1 = new ServiceZoneDTO();
        serviceZoneDTO1.setId(1L);
        ServiceZoneDTO serviceZoneDTO2 = new ServiceZoneDTO();
        assertThat(serviceZoneDTO1).isNotEqualTo(serviceZoneDTO2);
        serviceZoneDTO2.setId(serviceZoneDTO1.getId());
        assertThat(serviceZoneDTO1).isEqualTo(serviceZoneDTO2);
        serviceZoneDTO2.setId(2L);
        assertThat(serviceZoneDTO1).isNotEqualTo(serviceZoneDTO2);
        serviceZoneDTO1.setId(null);
        assertThat(serviceZoneDTO1).isNotEqualTo(serviceZoneDTO2);
    }
}

package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceAddonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceAddonDTO.class);
        ServiceAddonDTO serviceAddonDTO1 = new ServiceAddonDTO();
        serviceAddonDTO1.setId(1L);
        ServiceAddonDTO serviceAddonDTO2 = new ServiceAddonDTO();
        assertThat(serviceAddonDTO1).isNotEqualTo(serviceAddonDTO2);
        serviceAddonDTO2.setId(serviceAddonDTO1.getId());
        assertThat(serviceAddonDTO1).isEqualTo(serviceAddonDTO2);
        serviceAddonDTO2.setId(2L);
        assertThat(serviceAddonDTO1).isNotEqualTo(serviceAddonDTO2);
        serviceAddonDTO1.setId(null);
        assertThat(serviceAddonDTO1).isNotEqualTo(serviceAddonDTO2);
    }
}

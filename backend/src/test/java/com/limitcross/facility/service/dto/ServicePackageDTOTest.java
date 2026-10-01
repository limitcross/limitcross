package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicePackageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicePackageDTO.class);
        ServicePackageDTO servicePackageDTO1 = new ServicePackageDTO();
        servicePackageDTO1.setId(1L);
        ServicePackageDTO servicePackageDTO2 = new ServicePackageDTO();
        assertThat(servicePackageDTO1).isNotEqualTo(servicePackageDTO2);
        servicePackageDTO2.setId(servicePackageDTO1.getId());
        assertThat(servicePackageDTO1).isEqualTo(servicePackageDTO2);
        servicePackageDTO2.setId(2L);
        assertThat(servicePackageDTO1).isNotEqualTo(servicePackageDTO2);
        servicePackageDTO1.setId(null);
        assertThat(servicePackageDTO1).isNotEqualTo(servicePackageDTO2);
    }
}

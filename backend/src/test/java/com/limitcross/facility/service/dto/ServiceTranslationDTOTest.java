package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceTranslationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceTranslationDTO.class);
        ServiceTranslationDTO serviceTranslationDTO1 = new ServiceTranslationDTO();
        serviceTranslationDTO1.setId(1L);
        ServiceTranslationDTO serviceTranslationDTO2 = new ServiceTranslationDTO();
        assertThat(serviceTranslationDTO1).isNotEqualTo(serviceTranslationDTO2);
        serviceTranslationDTO2.setId(serviceTranslationDTO1.getId());
        assertThat(serviceTranslationDTO1).isEqualTo(serviceTranslationDTO2);
        serviceTranslationDTO2.setId(2L);
        assertThat(serviceTranslationDTO1).isNotEqualTo(serviceTranslationDTO2);
        serviceTranslationDTO1.setId(null);
        assertThat(serviceTranslationDTO1).isNotEqualTo(serviceTranslationDTO2);
    }
}

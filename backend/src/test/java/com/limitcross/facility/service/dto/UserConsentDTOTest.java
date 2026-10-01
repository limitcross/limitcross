package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserConsentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConsentDTO.class);
        UserConsentDTO userConsentDTO1 = new UserConsentDTO();
        userConsentDTO1.setId(1L);
        UserConsentDTO userConsentDTO2 = new UserConsentDTO();
        assertThat(userConsentDTO1).isNotEqualTo(userConsentDTO2);
        userConsentDTO2.setId(userConsentDTO1.getId());
        assertThat(userConsentDTO1).isEqualTo(userConsentDTO2);
        userConsentDTO2.setId(2L);
        assertThat(userConsentDTO1).isNotEqualTo(userConsentDTO2);
        userConsentDTO1.setId(null);
        assertThat(userConsentDTO1).isNotEqualTo(userConsentDTO2);
    }
}

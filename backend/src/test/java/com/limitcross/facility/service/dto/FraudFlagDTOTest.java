package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FraudFlagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FraudFlagDTO.class);
        FraudFlagDTO fraudFlagDTO1 = new FraudFlagDTO();
        fraudFlagDTO1.setId(1L);
        FraudFlagDTO fraudFlagDTO2 = new FraudFlagDTO();
        assertThat(fraudFlagDTO1).isNotEqualTo(fraudFlagDTO2);
        fraudFlagDTO2.setId(fraudFlagDTO1.getId());
        assertThat(fraudFlagDTO1).isEqualTo(fraudFlagDTO2);
        fraudFlagDTO2.setId(2L);
        assertThat(fraudFlagDTO1).isNotEqualTo(fraudFlagDTO2);
        fraudFlagDTO1.setId(null);
        assertThat(fraudFlagDTO1).isNotEqualTo(fraudFlagDTO2);
    }
}

package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeatureFlagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeatureFlagDTO.class);
        FeatureFlagDTO featureFlagDTO1 = new FeatureFlagDTO();
        featureFlagDTO1.setId(1L);
        FeatureFlagDTO featureFlagDTO2 = new FeatureFlagDTO();
        assertThat(featureFlagDTO1).isNotEqualTo(featureFlagDTO2);
        featureFlagDTO2.setId(featureFlagDTO1.getId());
        assertThat(featureFlagDTO1).isEqualTo(featureFlagDTO2);
        featureFlagDTO2.setId(2L);
        assertThat(featureFlagDTO1).isNotEqualTo(featureFlagDTO2);
        featureFlagDTO1.setId(null);
        assertThat(featureFlagDTO1).isNotEqualTo(featureFlagDTO2);
    }
}

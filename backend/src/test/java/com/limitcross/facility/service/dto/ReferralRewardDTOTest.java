package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferralRewardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferralRewardDTO.class);
        ReferralRewardDTO referralRewardDTO1 = new ReferralRewardDTO();
        referralRewardDTO1.setId(1L);
        ReferralRewardDTO referralRewardDTO2 = new ReferralRewardDTO();
        assertThat(referralRewardDTO1).isNotEqualTo(referralRewardDTO2);
        referralRewardDTO2.setId(referralRewardDTO1.getId());
        assertThat(referralRewardDTO1).isEqualTo(referralRewardDTO2);
        referralRewardDTO2.setId(2L);
        assertThat(referralRewardDTO1).isNotEqualTo(referralRewardDTO2);
        referralRewardDTO1.setId(null);
        assertThat(referralRewardDTO1).isNotEqualTo(referralRewardDTO2);
    }
}

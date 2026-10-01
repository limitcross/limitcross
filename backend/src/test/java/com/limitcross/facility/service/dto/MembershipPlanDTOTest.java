package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipPlanDTO.class);
        MembershipPlanDTO membershipPlanDTO1 = new MembershipPlanDTO();
        membershipPlanDTO1.setId(1L);
        MembershipPlanDTO membershipPlanDTO2 = new MembershipPlanDTO();
        assertThat(membershipPlanDTO1).isNotEqualTo(membershipPlanDTO2);
        membershipPlanDTO2.setId(membershipPlanDTO1.getId());
        assertThat(membershipPlanDTO1).isEqualTo(membershipPlanDTO2);
        membershipPlanDTO2.setId(2L);
        assertThat(membershipPlanDTO1).isNotEqualTo(membershipPlanDTO2);
        membershipPlanDTO1.setId(null);
        assertThat(membershipPlanDTO1).isNotEqualTo(membershipPlanDTO2);
    }
}

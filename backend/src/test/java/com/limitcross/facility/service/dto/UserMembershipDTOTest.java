package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMembershipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMembershipDTO.class);
        UserMembershipDTO userMembershipDTO1 = new UserMembershipDTO();
        userMembershipDTO1.setId(1L);
        UserMembershipDTO userMembershipDTO2 = new UserMembershipDTO();
        assertThat(userMembershipDTO1).isNotEqualTo(userMembershipDTO2);
        userMembershipDTO2.setId(userMembershipDTO1.getId());
        assertThat(userMembershipDTO1).isEqualTo(userMembershipDTO2);
        userMembershipDTO2.setId(2L);
        assertThat(userMembershipDTO1).isNotEqualTo(userMembershipDTO2);
        userMembershipDTO1.setId(null);
        assertThat(userMembershipDTO1).isNotEqualTo(userMembershipDTO2);
    }
}

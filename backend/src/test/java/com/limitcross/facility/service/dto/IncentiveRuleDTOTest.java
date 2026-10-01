package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncentiveRuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncentiveRuleDTO.class);
        IncentiveRuleDTO incentiveRuleDTO1 = new IncentiveRuleDTO();
        incentiveRuleDTO1.setId(1L);
        IncentiveRuleDTO incentiveRuleDTO2 = new IncentiveRuleDTO();
        assertThat(incentiveRuleDTO1).isNotEqualTo(incentiveRuleDTO2);
        incentiveRuleDTO2.setId(incentiveRuleDTO1.getId());
        assertThat(incentiveRuleDTO1).isEqualTo(incentiveRuleDTO2);
        incentiveRuleDTO2.setId(2L);
        assertThat(incentiveRuleDTO1).isNotEqualTo(incentiveRuleDTO2);
        incentiveRuleDTO1.setId(null);
        assertThat(incentiveRuleDTO1).isNotEqualTo(incentiveRuleDTO2);
    }
}

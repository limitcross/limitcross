package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommissionRuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommissionRuleDTO.class);
        CommissionRuleDTO commissionRuleDTO1 = new CommissionRuleDTO();
        commissionRuleDTO1.setId(1L);
        CommissionRuleDTO commissionRuleDTO2 = new CommissionRuleDTO();
        assertThat(commissionRuleDTO1).isNotEqualTo(commissionRuleDTO2);
        commissionRuleDTO2.setId(commissionRuleDTO1.getId());
        assertThat(commissionRuleDTO1).isEqualTo(commissionRuleDTO2);
        commissionRuleDTO2.setId(2L);
        assertThat(commissionRuleDTO1).isNotEqualTo(commissionRuleDTO2);
        commissionRuleDTO1.setId(null);
        assertThat(commissionRuleDTO1).isNotEqualTo(commissionRuleDTO2);
    }
}

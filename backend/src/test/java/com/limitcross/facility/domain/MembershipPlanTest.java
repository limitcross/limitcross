package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.MembershipPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipPlan.class);
        MembershipPlan membershipPlan1 = getMembershipPlanSample1();
        MembershipPlan membershipPlan2 = new MembershipPlan();
        assertThat(membershipPlan1).isNotEqualTo(membershipPlan2);

        membershipPlan2.setId(membershipPlan1.getId());
        assertThat(membershipPlan1).isEqualTo(membershipPlan2);

        membershipPlan2 = getMembershipPlanSample2();
        assertThat(membershipPlan1).isNotEqualTo(membershipPlan2);
    }
}

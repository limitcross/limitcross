package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.MembershipPlanTestSamples.*;
import static com.limitcross.facility.domain.UserMembershipTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMembershipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMembership.class);
        UserMembership userMembership1 = getUserMembershipSample1();
        UserMembership userMembership2 = new UserMembership();
        assertThat(userMembership1).isNotEqualTo(userMembership2);

        userMembership2.setId(userMembership1.getId());
        assertThat(userMembership1).isEqualTo(userMembership2);

        userMembership2 = getUserMembershipSample2();
        assertThat(userMembership1).isNotEqualTo(userMembership2);
    }

    @Test
    void planTest() {
        UserMembership userMembership = getUserMembershipRandomSampleGenerator();
        MembershipPlan membershipPlanBack = getMembershipPlanRandomSampleGenerator();

        userMembership.setPlan(membershipPlanBack);
        assertThat(userMembership.getPlan()).isEqualTo(membershipPlanBack);

        userMembership.plan(null);
        assertThat(userMembership.getPlan()).isNull();
    }
}

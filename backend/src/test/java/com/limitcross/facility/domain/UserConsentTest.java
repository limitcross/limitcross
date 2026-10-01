package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.UserConsentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserConsentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConsent.class);
        UserConsent userConsent1 = getUserConsentSample1();
        UserConsent userConsent2 = new UserConsent();
        assertThat(userConsent1).isNotEqualTo(userConsent2);

        userConsent2.setId(userConsent1.getId());
        assertThat(userConsent1).isEqualTo(userConsent2);

        userConsent2 = getUserConsentSample2();
        assertThat(userConsent1).isNotEqualTo(userConsent2);
    }
}

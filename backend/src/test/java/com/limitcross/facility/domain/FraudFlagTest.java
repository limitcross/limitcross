package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FraudFlagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FraudFlagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FraudFlag.class);
        FraudFlag fraudFlag1 = getFraudFlagSample1();
        FraudFlag fraudFlag2 = new FraudFlag();
        assertThat(fraudFlag1).isNotEqualTo(fraudFlag2);

        fraudFlag2.setId(fraudFlag1.getId());
        assertThat(fraudFlag1).isEqualTo(fraudFlag2);

        fraudFlag2 = getFraudFlagSample2();
        assertThat(fraudFlag1).isNotEqualTo(fraudFlag2);
    }
}

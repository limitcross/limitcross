package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ApiIdempotencyKeyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApiIdempotencyKeyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiIdempotencyKey.class);
        ApiIdempotencyKey apiIdempotencyKey1 = getApiIdempotencyKeySample1();
        ApiIdempotencyKey apiIdempotencyKey2 = new ApiIdempotencyKey();
        assertThat(apiIdempotencyKey1).isNotEqualTo(apiIdempotencyKey2);

        apiIdempotencyKey2.setId(apiIdempotencyKey1.getId());
        assertThat(apiIdempotencyKey1).isEqualTo(apiIdempotencyKey2);

        apiIdempotencyKey2 = getApiIdempotencyKeySample2();
        assertThat(apiIdempotencyKey1).isNotEqualTo(apiIdempotencyKey2);
    }
}

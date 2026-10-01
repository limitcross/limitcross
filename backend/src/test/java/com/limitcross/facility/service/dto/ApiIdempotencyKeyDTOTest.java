package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApiIdempotencyKeyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiIdempotencyKeyDTO.class);
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO1 = new ApiIdempotencyKeyDTO();
        apiIdempotencyKeyDTO1.setId(1L);
        ApiIdempotencyKeyDTO apiIdempotencyKeyDTO2 = new ApiIdempotencyKeyDTO();
        assertThat(apiIdempotencyKeyDTO1).isNotEqualTo(apiIdempotencyKeyDTO2);
        apiIdempotencyKeyDTO2.setId(apiIdempotencyKeyDTO1.getId());
        assertThat(apiIdempotencyKeyDTO1).isEqualTo(apiIdempotencyKeyDTO2);
        apiIdempotencyKeyDTO2.setId(2L);
        assertThat(apiIdempotencyKeyDTO1).isNotEqualTo(apiIdempotencyKeyDTO2);
        apiIdempotencyKeyDTO1.setId(null);
        assertThat(apiIdempotencyKeyDTO1).isNotEqualTo(apiIdempotencyKeyDTO2);
    }
}

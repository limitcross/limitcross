package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CouponRedemptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouponRedemptionDTO.class);
        CouponRedemptionDTO couponRedemptionDTO1 = new CouponRedemptionDTO();
        couponRedemptionDTO1.setId(1L);
        CouponRedemptionDTO couponRedemptionDTO2 = new CouponRedemptionDTO();
        assertThat(couponRedemptionDTO1).isNotEqualTo(couponRedemptionDTO2);
        couponRedemptionDTO2.setId(couponRedemptionDTO1.getId());
        assertThat(couponRedemptionDTO1).isEqualTo(couponRedemptionDTO2);
        couponRedemptionDTO2.setId(2L);
        assertThat(couponRedemptionDTO1).isNotEqualTo(couponRedemptionDTO2);
        couponRedemptionDTO1.setId(null);
        assertThat(couponRedemptionDTO1).isNotEqualTo(couponRedemptionDTO2);
    }
}

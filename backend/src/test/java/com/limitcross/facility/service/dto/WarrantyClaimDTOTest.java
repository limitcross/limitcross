package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WarrantyClaimDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarrantyClaimDTO.class);
        WarrantyClaimDTO warrantyClaimDTO1 = new WarrantyClaimDTO();
        warrantyClaimDTO1.setId(1L);
        WarrantyClaimDTO warrantyClaimDTO2 = new WarrantyClaimDTO();
        assertThat(warrantyClaimDTO1).isNotEqualTo(warrantyClaimDTO2);
        warrantyClaimDTO2.setId(warrantyClaimDTO1.getId());
        assertThat(warrantyClaimDTO1).isEqualTo(warrantyClaimDTO2);
        warrantyClaimDTO2.setId(2L);
        assertThat(warrantyClaimDTO1).isNotEqualTo(warrantyClaimDTO2);
        warrantyClaimDTO1.setId(null);
        assertThat(warrantyClaimDTO1).isNotEqualTo(warrantyClaimDTO2);
    }
}

package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoyaltyLedgerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoyaltyLedgerDTO.class);
        LoyaltyLedgerDTO loyaltyLedgerDTO1 = new LoyaltyLedgerDTO();
        loyaltyLedgerDTO1.setId(1L);
        LoyaltyLedgerDTO loyaltyLedgerDTO2 = new LoyaltyLedgerDTO();
        assertThat(loyaltyLedgerDTO1).isNotEqualTo(loyaltyLedgerDTO2);
        loyaltyLedgerDTO2.setId(loyaltyLedgerDTO1.getId());
        assertThat(loyaltyLedgerDTO1).isEqualTo(loyaltyLedgerDTO2);
        loyaltyLedgerDTO2.setId(2L);
        assertThat(loyaltyLedgerDTO1).isNotEqualTo(loyaltyLedgerDTO2);
        loyaltyLedgerDTO1.setId(null);
        assertThat(loyaltyLedgerDTO1).isNotEqualTo(loyaltyLedgerDTO2);
    }
}

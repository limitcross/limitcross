package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlotHoldDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotHoldDTO.class);
        SlotHoldDTO slotHoldDTO1 = new SlotHoldDTO();
        slotHoldDTO1.setId(1L);
        SlotHoldDTO slotHoldDTO2 = new SlotHoldDTO();
        assertThat(slotHoldDTO1).isNotEqualTo(slotHoldDTO2);
        slotHoldDTO2.setId(slotHoldDTO1.getId());
        assertThat(slotHoldDTO1).isEqualTo(slotHoldDTO2);
        slotHoldDTO2.setId(2L);
        assertThat(slotHoldDTO1).isNotEqualTo(slotHoldDTO2);
        slotHoldDTO1.setId(null);
        assertThat(slotHoldDTO1).isNotEqualTo(slotHoldDTO2);
    }
}

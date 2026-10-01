package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlotCapacityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotCapacityDTO.class);
        SlotCapacityDTO slotCapacityDTO1 = new SlotCapacityDTO();
        slotCapacityDTO1.setId(1L);
        SlotCapacityDTO slotCapacityDTO2 = new SlotCapacityDTO();
        assertThat(slotCapacityDTO1).isNotEqualTo(slotCapacityDTO2);
        slotCapacityDTO2.setId(slotCapacityDTO1.getId());
        assertThat(slotCapacityDTO1).isEqualTo(slotCapacityDTO2);
        slotCapacityDTO2.setId(2L);
        assertThat(slotCapacityDTO1).isNotEqualTo(slotCapacityDTO2);
        slotCapacityDTO1.setId(null);
        assertThat(slotCapacityDTO1).isNotEqualTo(slotCapacityDTO2);
    }
}

package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlockedEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlockedEntityDTO.class);
        BlockedEntityDTO blockedEntityDTO1 = new BlockedEntityDTO();
        blockedEntityDTO1.setId(1L);
        BlockedEntityDTO blockedEntityDTO2 = new BlockedEntityDTO();
        assertThat(blockedEntityDTO1).isNotEqualTo(blockedEntityDTO2);
        blockedEntityDTO2.setId(blockedEntityDTO1.getId());
        assertThat(blockedEntityDTO1).isEqualTo(blockedEntityDTO2);
        blockedEntityDTO2.setId(2L);
        assertThat(blockedEntityDTO1).isNotEqualTo(blockedEntityDTO2);
        blockedEntityDTO1.setId(null);
        assertThat(blockedEntityDTO1).isNotEqualTo(blockedEntityDTO2);
    }
}

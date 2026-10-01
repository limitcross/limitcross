package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.BlockedEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlockedEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlockedEntity.class);
        BlockedEntity blockedEntity1 = getBlockedEntitySample1();
        BlockedEntity blockedEntity2 = new BlockedEntity();
        assertThat(blockedEntity1).isNotEqualTo(blockedEntity2);

        blockedEntity2.setId(blockedEntity1.getId());
        assertThat(blockedEntity1).isEqualTo(blockedEntity2);

        blockedEntity2 = getBlockedEntitySample2();
        assertThat(blockedEntity1).isNotEqualTo(blockedEntity2);
    }
}

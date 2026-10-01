package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ServiceCategoryTestSamples.*;
import static com.limitcross.facility.domain.ServiceZoneTestSamples.*;
import static com.limitcross.facility.domain.SlotCapacityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlotCapacityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotCapacity.class);
        SlotCapacity slotCapacity1 = getSlotCapacitySample1();
        SlotCapacity slotCapacity2 = new SlotCapacity();
        assertThat(slotCapacity1).isNotEqualTo(slotCapacity2);

        slotCapacity2.setId(slotCapacity1.getId());
        assertThat(slotCapacity1).isEqualTo(slotCapacity2);

        slotCapacity2 = getSlotCapacitySample2();
        assertThat(slotCapacity1).isNotEqualTo(slotCapacity2);
    }

    @Test
    void zoneTest() {
        SlotCapacity slotCapacity = getSlotCapacityRandomSampleGenerator();
        ServiceZone serviceZoneBack = getServiceZoneRandomSampleGenerator();

        slotCapacity.setZone(serviceZoneBack);
        assertThat(slotCapacity.getZone()).isEqualTo(serviceZoneBack);

        slotCapacity.zone(null);
        assertThat(slotCapacity.getZone()).isNull();
    }

    @Test
    void categoryTest() {
        SlotCapacity slotCapacity = getSlotCapacityRandomSampleGenerator();
        ServiceCategory serviceCategoryBack = getServiceCategoryRandomSampleGenerator();

        slotCapacity.setCategory(serviceCategoryBack);
        assertThat(slotCapacity.getCategory()).isEqualTo(serviceCategoryBack);

        slotCapacity.category(null);
        assertThat(slotCapacity.getCategory()).isNull();
    }
}

package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.FacilityServiceTestSamples.*;
import static com.limitcross.facility.domain.TrainingModuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingModuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingModule.class);
        TrainingModule trainingModule1 = getTrainingModuleSample1();
        TrainingModule trainingModule2 = new TrainingModule();
        assertThat(trainingModule1).isNotEqualTo(trainingModule2);

        trainingModule2.setId(trainingModule1.getId());
        assertThat(trainingModule1).isEqualTo(trainingModule2);

        trainingModule2 = getTrainingModuleSample2();
        assertThat(trainingModule1).isNotEqualTo(trainingModule2);
    }

    @Test
    void serviceTest() {
        TrainingModule trainingModule = getTrainingModuleRandomSampleGenerator();
        FacilityService facilityServiceBack = getFacilityServiceRandomSampleGenerator();

        trainingModule.setService(facilityServiceBack);
        assertThat(trainingModule.getService()).isEqualTo(facilityServiceBack);

        trainingModule.service(null);
        assertThat(trainingModule.getService()).isNull();
    }
}

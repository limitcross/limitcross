package com.limitcross.facility.domain;

import static com.limitcross.facility.domain.ProfessionalTestSamples.*;
import static com.limitcross.facility.domain.ProfessionalTrainingTestSamples.*;
import static com.limitcross.facility.domain.TrainingModuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalTrainingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalTraining.class);
        ProfessionalTraining professionalTraining1 = getProfessionalTrainingSample1();
        ProfessionalTraining professionalTraining2 = new ProfessionalTraining();
        assertThat(professionalTraining1).isNotEqualTo(professionalTraining2);

        professionalTraining2.setId(professionalTraining1.getId());
        assertThat(professionalTraining1).isEqualTo(professionalTraining2);

        professionalTraining2 = getProfessionalTrainingSample2();
        assertThat(professionalTraining1).isNotEqualTo(professionalTraining2);
    }

    @Test
    void professionalTest() {
        ProfessionalTraining professionalTraining = getProfessionalTrainingRandomSampleGenerator();
        Professional professionalBack = getProfessionalRandomSampleGenerator();

        professionalTraining.setProfessional(professionalBack);
        assertThat(professionalTraining.getProfessional()).isEqualTo(professionalBack);

        professionalTraining.professional(null);
        assertThat(professionalTraining.getProfessional()).isNull();
    }

    @Test
    void moduleTest() {
        ProfessionalTraining professionalTraining = getProfessionalTrainingRandomSampleGenerator();
        TrainingModule trainingModuleBack = getTrainingModuleRandomSampleGenerator();

        professionalTraining.setModule(trainingModuleBack);
        assertThat(professionalTraining.getModule()).isEqualTo(trainingModuleBack);

        professionalTraining.module(null);
        assertThat(professionalTraining.getModule()).isNull();
    }
}

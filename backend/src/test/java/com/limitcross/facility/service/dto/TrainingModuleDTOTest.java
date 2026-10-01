package com.limitcross.facility.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.limitcross.facility.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingModuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingModuleDTO.class);
        TrainingModuleDTO trainingModuleDTO1 = new TrainingModuleDTO();
        trainingModuleDTO1.setId(1L);
        TrainingModuleDTO trainingModuleDTO2 = new TrainingModuleDTO();
        assertThat(trainingModuleDTO1).isNotEqualTo(trainingModuleDTO2);
        trainingModuleDTO2.setId(trainingModuleDTO1.getId());
        assertThat(trainingModuleDTO1).isEqualTo(trainingModuleDTO2);
        trainingModuleDTO2.setId(2L);
        assertThat(trainingModuleDTO1).isNotEqualTo(trainingModuleDTO2);
        trainingModuleDTO1.setId(null);
        assertThat(trainingModuleDTO1).isNotEqualTo(trainingModuleDTO2);
    }
}

package com.limitcross.facility.service.mapper;

import static com.limitcross.facility.domain.TrainingModuleAsserts.*;
import static com.limitcross.facility.domain.TrainingModuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainingModuleMapperTest {

    private TrainingModuleMapper trainingModuleMapper;

    @BeforeEach
    void setUp() {
        trainingModuleMapper = new TrainingModuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrainingModuleSample1();
        var actual = trainingModuleMapper.toEntity(trainingModuleMapper.toDto(expected));
        assertTrainingModuleAllPropertiesEquals(expected, actual);
    }
}

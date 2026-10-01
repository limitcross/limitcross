package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalTraining;
import com.limitcross.facility.domain.TrainingModule;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalTrainingDTO;
import com.limitcross.facility.service.dto.TrainingModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfessionalTraining} and its DTO {@link ProfessionalTrainingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalTrainingMapper extends EntityMapper<ProfessionalTrainingDTO, ProfessionalTraining> {
    @Mapping(target = "professional", source = "professional", qualifiedByName = "professionalDisplayName")
    @Mapping(target = "module", source = "module", qualifiedByName = "trainingModuleTitle")
    ProfessionalTrainingDTO toDto(ProfessionalTraining s);

    @Named("professionalDisplayName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "displayName", source = "displayName")
    ProfessionalDTO toDtoProfessionalDisplayName(Professional professional);

    @Named("trainingModuleTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    TrainingModuleDTO toDtoTrainingModuleTitle(TrainingModule trainingModule);
}

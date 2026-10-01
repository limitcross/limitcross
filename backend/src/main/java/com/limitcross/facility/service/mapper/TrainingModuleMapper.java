package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.TrainingModule;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.TrainingModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrainingModule} and its DTO {@link TrainingModuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrainingModuleMapper extends EntityMapper<TrainingModuleDTO, TrainingModule> {
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    TrainingModuleDTO toDto(TrainingModule s);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);
}

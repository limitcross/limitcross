package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServiceTranslation;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ServiceTranslationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceTranslation} and its DTO {@link ServiceTranslationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceTranslationMapper extends EntityMapper<ServiceTranslationDTO, ServiceTranslation> {
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    ServiceTranslationDTO toDto(ServiceTranslation s);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);
}

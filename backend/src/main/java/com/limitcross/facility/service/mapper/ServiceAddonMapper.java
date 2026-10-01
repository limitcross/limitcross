package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServiceAddon;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ServiceAddonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceAddon} and its DTO {@link ServiceAddonDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceAddonMapper extends EntityMapper<ServiceAddonDTO, ServiceAddon> {
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    ServiceAddonDTO toDto(ServiceAddon s);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);
}

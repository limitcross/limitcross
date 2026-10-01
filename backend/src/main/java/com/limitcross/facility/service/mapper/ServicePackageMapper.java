package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ServicePackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServicePackage} and its DTO {@link ServicePackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServicePackageMapper extends EntityMapper<ServicePackageDTO, ServicePackage> {
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    ServicePackageDTO toDto(ServicePackage s);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);
}

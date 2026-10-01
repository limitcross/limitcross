package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.CancellationPolicy;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.service.dto.CancellationPolicyDTO;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ServiceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CancellationPolicy} and its DTO {@link CancellationPolicyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CancellationPolicyMapper extends EntityMapper<CancellationPolicyDTO, CancellationPolicy> {
    @Mapping(target = "category", source = "category", qualifiedByName = "serviceCategoryName")
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    CancellationPolicyDTO toDto(CancellationPolicy s);

    @Named("serviceCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceCategoryDTO toDtoServiceCategoryName(ServiceCategory serviceCategory);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);
}

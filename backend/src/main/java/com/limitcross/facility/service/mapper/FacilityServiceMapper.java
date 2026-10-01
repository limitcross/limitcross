package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.dto.ServiceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacilityService} and its DTO {@link FacilityServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacilityServiceMapper extends EntityMapper<FacilityServiceDTO, FacilityService> {
    @Mapping(target = "category", source = "category", qualifiedByName = "serviceCategoryName")
    FacilityServiceDTO toDto(FacilityService s);

    @Named("serviceCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceCategoryDTO toDtoServiceCategoryName(ServiceCategory serviceCategory);
}

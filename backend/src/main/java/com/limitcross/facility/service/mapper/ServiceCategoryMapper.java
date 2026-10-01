package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.service.dto.ServiceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceCategory} and its DTO {@link ServiceCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceCategoryMapper extends EntityMapper<ServiceCategoryDTO, ServiceCategory> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "serviceCategoryName")
    ServiceCategoryDTO toDto(ServiceCategory s);

    @Named("serviceCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceCategoryDTO toDtoServiceCategoryName(ServiceCategory serviceCategory);
}

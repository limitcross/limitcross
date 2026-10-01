package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.domain.ServiceZone;
import com.limitcross.facility.domain.SlotCapacity;
import com.limitcross.facility.service.dto.ServiceCategoryDTO;
import com.limitcross.facility.service.dto.ServiceZoneDTO;
import com.limitcross.facility.service.dto.SlotCapacityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SlotCapacity} and its DTO {@link SlotCapacityDTO}.
 */
@Mapper(componentModel = "spring")
public interface SlotCapacityMapper extends EntityMapper<SlotCapacityDTO, SlotCapacity> {
    @Mapping(target = "zone", source = "zone", qualifiedByName = "serviceZoneName")
    @Mapping(target = "category", source = "category", qualifiedByName = "serviceCategoryName")
    SlotCapacityDTO toDto(SlotCapacity s);

    @Named("serviceZoneName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceZoneDTO toDtoServiceZoneName(ServiceZone serviceZone);

    @Named("serviceCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceCategoryDTO toDtoServiceCategoryName(ServiceCategory serviceCategory);
}

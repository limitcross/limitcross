package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.CityDailyMetrics;
import com.limitcross.facility.domain.ServiceCategory;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.CityDailyMetricsDTO;
import com.limitcross.facility.service.dto.ServiceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CityDailyMetrics} and its DTO {@link CityDailyMetricsDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityDailyMetricsMapper extends EntityMapper<CityDailyMetricsDTO, CityDailyMetrics> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    @Mapping(target = "category", source = "category", qualifiedByName = "serviceCategoryName")
    CityDailyMetricsDTO toDto(CityDailyMetrics s);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);

    @Named("serviceCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceCategoryDTO toDtoServiceCategoryName(ServiceCategory serviceCategory);
}

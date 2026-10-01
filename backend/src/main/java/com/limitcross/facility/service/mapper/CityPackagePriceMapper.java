package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.CityPackagePrice;
import com.limitcross.facility.domain.ServicePackage;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.CityPackagePriceDTO;
import com.limitcross.facility.service.dto.ServicePackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CityPackagePrice} and its DTO {@link CityPackagePriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityPackagePriceMapper extends EntityMapper<CityPackagePriceDTO, CityPackagePrice> {
    @Mapping(target = "servicePackage", source = "servicePackage", qualifiedByName = "servicePackageName")
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    CityPackagePriceDTO toDto(CityPackagePrice s);

    @Named("servicePackageName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServicePackageDTO toDtoServicePackageName(ServicePackage servicePackage);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);
}

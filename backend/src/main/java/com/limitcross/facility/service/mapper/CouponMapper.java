package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.Coupon;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.CouponDTO;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Coupon} and its DTO {@link CouponDTO}.
 */
@Mapper(componentModel = "spring")
public interface CouponMapper extends EntityMapper<CouponDTO, Coupon> {
    @Mapping(target = "service", source = "service", qualifiedByName = "facilityServiceTitle")
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    CouponDTO toDto(Coupon s);

    @Named("facilityServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    FacilityServiceDTO toDtoFacilityServiceTitle(FacilityService facilityService);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);
}

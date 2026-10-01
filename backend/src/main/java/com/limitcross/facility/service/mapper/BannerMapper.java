package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.Banner;
import com.limitcross.facility.domain.City;
import com.limitcross.facility.service.dto.BannerDTO;
import com.limitcross.facility.service.dto.CityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Banner} and its DTO {@link BannerDTO}.
 */
@Mapper(componentModel = "spring")
public interface BannerMapper extends EntityMapper<BannerDTO, Banner> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    BannerDTO toDto(Banner s);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);
}

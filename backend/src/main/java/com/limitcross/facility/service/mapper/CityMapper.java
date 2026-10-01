package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.service.dto.CityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {}

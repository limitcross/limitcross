package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.IncentiveRule;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.IncentiveRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IncentiveRule} and its DTO {@link IncentiveRuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface IncentiveRuleMapper extends EntityMapper<IncentiveRuleDTO, IncentiveRule> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    IncentiveRuleDTO toDto(IncentiveRule s);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);
}

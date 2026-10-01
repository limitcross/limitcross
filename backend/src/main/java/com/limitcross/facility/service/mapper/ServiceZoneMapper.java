package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ServiceZone;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ServiceZoneDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceZone} and its DTO {@link ServiceZoneDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceZoneMapper extends EntityMapper<ServiceZoneDTO, ServiceZone> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    @Mapping(target = "professionals", source = "professionals", qualifiedByName = "professionalIdSet")
    ServiceZoneDTO toDto(ServiceZone s);

    @Mapping(target = "professionals", ignore = true)
    @Mapping(target = "removeProfessional", ignore = true)
    ServiceZone toEntity(ServiceZoneDTO serviceZoneDTO);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);

    @Named("professionalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfessionalDTO toDtoProfessionalId(Professional professional);

    @Named("professionalIdSet")
    default Set<ProfessionalDTO> toDtoProfessionalIdSet(Set<Professional> professional) {
        return professional.stream().map(this::toDtoProfessionalId).collect(Collectors.toSet());
    }
}

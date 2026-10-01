package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalTier;
import com.limitcross.facility.domain.ServiceZone;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.dto.ProfessionalTierDTO;
import com.limitcross.facility.service.dto.ServiceZoneDTO;
import com.limitcross.facility.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Professional} and its DTO {@link ProfessionalDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalMapper extends EntityMapper<ProfessionalDTO, Professional> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "homeCity", source = "homeCity", qualifiedByName = "cityName")
    @Mapping(target = "tier", source = "tier", qualifiedByName = "professionalTierName")
    @Mapping(target = "zones", source = "zones", qualifiedByName = "serviceZoneNameSet")
    ProfessionalDTO toDto(Professional s);

    @Mapping(target = "removeZone", ignore = true)
    Professional toEntity(ProfessionalDTO professionalDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("cityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CityDTO toDtoCityName(City city);

    @Named("professionalTierName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProfessionalTierDTO toDtoProfessionalTierName(ProfessionalTier professionalTier);

    @Named("serviceZoneName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ServiceZoneDTO toDtoServiceZoneName(ServiceZone serviceZone);

    @Named("serviceZoneNameSet")
    default Set<ServiceZoneDTO> toDtoServiceZoneNameSet(Set<ServiceZone> serviceZone) {
        return serviceZone.stream().map(this::toDtoServiceZoneName).collect(Collectors.toSet());
    }
}

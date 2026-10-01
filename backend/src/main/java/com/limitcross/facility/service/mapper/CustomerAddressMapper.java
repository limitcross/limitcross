package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.City;
import com.limitcross.facility.domain.CustomerAddress;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.CityDTO;
import com.limitcross.facility.service.dto.CustomerAddressDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerAddress} and its DTO {@link CustomerAddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerAddressMapper extends EntityMapper<CustomerAddressDTO, CustomerAddress> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userLogin")
    @Mapping(target = "city", source = "city", qualifiedByName = "cityName")
    CustomerAddressDTO toDto(CustomerAddress s);

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
}

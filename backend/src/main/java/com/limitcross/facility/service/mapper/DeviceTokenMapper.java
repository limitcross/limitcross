package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.DeviceToken;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.DeviceTokenDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceToken} and its DTO {@link DeviceTokenDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceTokenMapper extends EntityMapper<DeviceTokenDTO, DeviceToken> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    DeviceTokenDTO toDto(DeviceToken s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

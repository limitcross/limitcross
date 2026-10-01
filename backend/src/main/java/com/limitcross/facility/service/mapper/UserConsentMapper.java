package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.UserConsent;
import com.limitcross.facility.service.dto.UserConsentDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserConsent} and its DTO {@link UserConsentDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserConsentMapper extends EntityMapper<UserConsentDTO, UserConsent> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    UserConsentDTO toDto(UserConsent s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

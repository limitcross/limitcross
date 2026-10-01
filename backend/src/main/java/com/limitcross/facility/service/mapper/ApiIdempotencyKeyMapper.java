package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.ApiIdempotencyKey;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.ApiIdempotencyKeyDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApiIdempotencyKey} and its DTO {@link ApiIdempotencyKeyDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApiIdempotencyKeyMapper extends EntityMapper<ApiIdempotencyKeyDTO, ApiIdempotencyKey> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ApiIdempotencyKeyDTO toDto(ApiIdempotencyKey s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

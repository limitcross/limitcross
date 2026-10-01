package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.DataDeletionRequest;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.DataDeletionRequestDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DataDeletionRequest} and its DTO {@link DataDeletionRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface DataDeletionRequestMapper extends EntityMapper<DataDeletionRequestDTO, DataDeletionRequest> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    DataDeletionRequestDTO toDto(DataDeletionRequest s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

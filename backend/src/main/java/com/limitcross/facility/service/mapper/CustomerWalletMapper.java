package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.CustomerWallet;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.service.dto.CustomerWalletDTO;
import com.limitcross.facility.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerWallet} and its DTO {@link CustomerWalletDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerWalletMapper extends EntityMapper<CustomerWalletDTO, CustomerWallet> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    CustomerWalletDTO toDto(CustomerWallet s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

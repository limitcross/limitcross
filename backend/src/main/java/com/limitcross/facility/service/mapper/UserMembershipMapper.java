package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.MembershipPlan;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.UserMembership;
import com.limitcross.facility.service.dto.MembershipPlanDTO;
import com.limitcross.facility.service.dto.UserDTO;
import com.limitcross.facility.service.dto.UserMembershipDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserMembership} and its DTO {@link UserMembershipDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserMembershipMapper extends EntityMapper<UserMembershipDTO, UserMembership> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "membershipPlanName")
    UserMembershipDTO toDto(UserMembership s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("membershipPlanName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MembershipPlanDTO toDtoMembershipPlanName(MembershipPlan membershipPlan);
}

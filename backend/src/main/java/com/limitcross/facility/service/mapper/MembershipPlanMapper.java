package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.MembershipPlan;
import com.limitcross.facility.service.dto.MembershipPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipPlan} and its DTO {@link MembershipPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface MembershipPlanMapper extends EntityMapper<MembershipPlanDTO, MembershipPlan> {}

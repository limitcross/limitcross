package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.FeatureFlag;
import com.limitcross.facility.service.dto.FeatureFlagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FeatureFlag} and its DTO {@link FeatureFlagDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeatureFlagMapper extends EntityMapper<FeatureFlagDTO, FeatureFlag> {}

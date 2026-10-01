package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.OutboxEvent;
import com.limitcross.facility.service.dto.OutboxEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OutboxEvent} and its DTO {@link OutboxEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface OutboxEventMapper extends EntityMapper<OutboxEventDTO, OutboxEvent> {}

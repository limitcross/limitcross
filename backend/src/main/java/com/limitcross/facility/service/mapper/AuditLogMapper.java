package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.AuditLog;
import com.limitcross.facility.service.dto.AuditLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuditLog} and its DTO {@link AuditLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuditLogMapper extends EntityMapper<AuditLogDTO, AuditLog> {}

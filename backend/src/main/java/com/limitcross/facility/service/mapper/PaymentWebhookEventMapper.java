package com.limitcross.facility.service.mapper;

import com.limitcross.facility.domain.PaymentWebhookEvent;
import com.limitcross.facility.service.dto.PaymentWebhookEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentWebhookEvent} and its DTO {@link PaymentWebhookEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentWebhookEventMapper extends EntityMapper<PaymentWebhookEventDTO, PaymentWebhookEvent> {}

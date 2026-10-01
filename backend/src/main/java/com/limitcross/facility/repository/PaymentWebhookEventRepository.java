package com.limitcross.facility.repository;

import com.limitcross.facility.domain.PaymentWebhookEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentWebhookEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentWebhookEventRepository extends JpaRepository<PaymentWebhookEvent, Long> {}

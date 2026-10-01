package com.limitcross.facility.repository;

import com.limitcross.facility.domain.OutboxEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OutboxEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {}

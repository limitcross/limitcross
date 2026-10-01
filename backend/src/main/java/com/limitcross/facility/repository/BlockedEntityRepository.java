package com.limitcross.facility.repository;

import com.limitcross.facility.domain.BlockedEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BlockedEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockedEntityRepository extends JpaRepository<BlockedEntity, Long> {}

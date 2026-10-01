package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalTier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalTier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionalTierRepository extends JpaRepository<ProfessionalTier, Long> {}

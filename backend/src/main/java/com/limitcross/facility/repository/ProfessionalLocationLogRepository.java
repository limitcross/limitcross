package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalLocationLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalLocationLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionalLocationLogRepository extends JpaRepository<ProfessionalLocationLog, Long> {}

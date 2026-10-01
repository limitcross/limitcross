package com.limitcross.facility.repository;

import com.limitcross.facility.domain.FraudFlag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FraudFlag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FraudFlagRepository extends JpaRepository<FraudFlag, Long> {}

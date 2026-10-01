package com.limitcross.facility.repository;

import com.limitcross.facility.domain.MembershipPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MembershipPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {}

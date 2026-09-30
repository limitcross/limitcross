package com.limitcross.facility.repository;

import com.limitcross.facility.domain.FacilityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityServiceRepository extends JpaRepository<FacilityService, String> {
}
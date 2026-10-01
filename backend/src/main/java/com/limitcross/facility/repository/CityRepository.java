package com.limitcross.facility.repository;

import com.limitcross.facility.domain.City;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the City entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
	Optional<City> findFirstByActiveTrueOrderByIdAsc();
}

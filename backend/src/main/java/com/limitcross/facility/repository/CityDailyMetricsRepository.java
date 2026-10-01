package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CityDailyMetrics;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CityDailyMetrics entity.
 */
@Repository
public interface CityDailyMetricsRepository extends JpaRepository<CityDailyMetrics, Long> {
    default Optional<CityDailyMetrics> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CityDailyMetrics> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CityDailyMetrics> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cityDailyMetrics from CityDailyMetrics cityDailyMetrics left join fetch cityDailyMetrics.city left join fetch cityDailyMetrics.category",
        countQuery = "select count(cityDailyMetrics) from CityDailyMetrics cityDailyMetrics"
    )
    Page<CityDailyMetrics> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cityDailyMetrics from CityDailyMetrics cityDailyMetrics left join fetch cityDailyMetrics.city left join fetch cityDailyMetrics.category"
    )
    List<CityDailyMetrics> findAllWithToOneRelationships();

    @Query(
        "select cityDailyMetrics from CityDailyMetrics cityDailyMetrics left join fetch cityDailyMetrics.city left join fetch cityDailyMetrics.category where cityDailyMetrics.id =:id"
    )
    Optional<CityDailyMetrics> findOneWithToOneRelationships(@Param("id") Long id);
}

package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalDailyMetrics;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalDailyMetrics entity.
 */
@Repository
public interface ProfessionalDailyMetricsRepository extends JpaRepository<ProfessionalDailyMetrics, Long> {
    default Optional<ProfessionalDailyMetrics> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalDailyMetrics> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalDailyMetrics> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalDailyMetrics from ProfessionalDailyMetrics professionalDailyMetrics left join fetch professionalDailyMetrics.professional",
        countQuery = "select count(professionalDailyMetrics) from ProfessionalDailyMetrics professionalDailyMetrics"
    )
    Page<ProfessionalDailyMetrics> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professionalDailyMetrics from ProfessionalDailyMetrics professionalDailyMetrics left join fetch professionalDailyMetrics.professional"
    )
    List<ProfessionalDailyMetrics> findAllWithToOneRelationships();

    @Query(
        "select professionalDailyMetrics from ProfessionalDailyMetrics professionalDailyMetrics left join fetch professionalDailyMetrics.professional where professionalDailyMetrics.id =:id"
    )
    Optional<ProfessionalDailyMetrics> findOneWithToOneRelationships(@Param("id") Long id);
}

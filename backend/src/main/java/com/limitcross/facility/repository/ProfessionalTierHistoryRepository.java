package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalTierHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalTierHistory entity.
 */
@Repository
public interface ProfessionalTierHistoryRepository extends JpaRepository<ProfessionalTierHistory, Long> {
    default Optional<ProfessionalTierHistory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalTierHistory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalTierHistory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalTierHistory from ProfessionalTierHistory professionalTierHistory left join fetch professionalTierHistory.professional left join fetch professionalTierHistory.tier",
        countQuery = "select count(professionalTierHistory) from ProfessionalTierHistory professionalTierHistory"
    )
    Page<ProfessionalTierHistory> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professionalTierHistory from ProfessionalTierHistory professionalTierHistory left join fetch professionalTierHistory.professional left join fetch professionalTierHistory.tier"
    )
    List<ProfessionalTierHistory> findAllWithToOneRelationships();

    @Query(
        "select professionalTierHistory from ProfessionalTierHistory professionalTierHistory left join fetch professionalTierHistory.professional left join fetch professionalTierHistory.tier where professionalTierHistory.id =:id"
    )
    Optional<ProfessionalTierHistory> findOneWithToOneRelationships(@Param("id") Long id);
}

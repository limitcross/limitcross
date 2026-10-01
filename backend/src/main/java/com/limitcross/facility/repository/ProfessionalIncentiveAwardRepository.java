package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalIncentiveAward;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalIncentiveAward entity.
 */
@Repository
public interface ProfessionalIncentiveAwardRepository extends JpaRepository<ProfessionalIncentiveAward, Long> {
    default Optional<ProfessionalIncentiveAward> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalIncentiveAward> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalIncentiveAward> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalIncentiveAward from ProfessionalIncentiveAward professionalIncentiveAward left join fetch professionalIncentiveAward.professional left join fetch professionalIncentiveAward.rule",
        countQuery = "select count(professionalIncentiveAward) from ProfessionalIncentiveAward professionalIncentiveAward"
    )
    Page<ProfessionalIncentiveAward> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professionalIncentiveAward from ProfessionalIncentiveAward professionalIncentiveAward left join fetch professionalIncentiveAward.professional left join fetch professionalIncentiveAward.rule"
    )
    List<ProfessionalIncentiveAward> findAllWithToOneRelationships();

    @Query(
        "select professionalIncentiveAward from ProfessionalIncentiveAward professionalIncentiveAward left join fetch professionalIncentiveAward.professional left join fetch professionalIncentiveAward.rule where professionalIncentiveAward.id =:id"
    )
    Optional<ProfessionalIncentiveAward> findOneWithToOneRelationships(@Param("id") Long id);
}

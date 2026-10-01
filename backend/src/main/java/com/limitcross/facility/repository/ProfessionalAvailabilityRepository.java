package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalAvailability;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalAvailability entity.
 */
@Repository
public interface ProfessionalAvailabilityRepository extends JpaRepository<ProfessionalAvailability, Long> {
    default Optional<ProfessionalAvailability> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalAvailability> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalAvailability> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalAvailability from ProfessionalAvailability professionalAvailability left join fetch professionalAvailability.professional",
        countQuery = "select count(professionalAvailability) from ProfessionalAvailability professionalAvailability"
    )
    Page<ProfessionalAvailability> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professionalAvailability from ProfessionalAvailability professionalAvailability left join fetch professionalAvailability.professional"
    )
    List<ProfessionalAvailability> findAllWithToOneRelationships();

    @Query(
        "select professionalAvailability from ProfessionalAvailability professionalAvailability left join fetch professionalAvailability.professional where professionalAvailability.id =:id"
    )
    Optional<ProfessionalAvailability> findOneWithToOneRelationships(@Param("id") Long id);
}

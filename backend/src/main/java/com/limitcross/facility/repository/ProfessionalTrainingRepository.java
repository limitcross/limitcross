package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalTraining;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalTraining entity.
 */
@Repository
public interface ProfessionalTrainingRepository extends JpaRepository<ProfessionalTraining, Long> {
    default Optional<ProfessionalTraining> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalTraining> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalTraining> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalTraining from ProfessionalTraining professionalTraining left join fetch professionalTraining.professional left join fetch professionalTraining.module",
        countQuery = "select count(professionalTraining) from ProfessionalTraining professionalTraining"
    )
    Page<ProfessionalTraining> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professionalTraining from ProfessionalTraining professionalTraining left join fetch professionalTraining.professional left join fetch professionalTraining.module"
    )
    List<ProfessionalTraining> findAllWithToOneRelationships();

    @Query(
        "select professionalTraining from ProfessionalTraining professionalTraining left join fetch professionalTraining.professional left join fetch professionalTraining.module where professionalTraining.id =:id"
    )
    Optional<ProfessionalTraining> findOneWithToOneRelationships(@Param("id") Long id);
}

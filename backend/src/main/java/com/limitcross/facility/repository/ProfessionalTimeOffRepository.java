package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalTimeOff;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalTimeOff entity.
 */
@Repository
public interface ProfessionalTimeOffRepository extends JpaRepository<ProfessionalTimeOff, Long> {
    default Optional<ProfessionalTimeOff> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalTimeOff> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalTimeOff> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalTimeOff from ProfessionalTimeOff professionalTimeOff left join fetch professionalTimeOff.booking left join fetch professionalTimeOff.professional",
        countQuery = "select count(professionalTimeOff) from ProfessionalTimeOff professionalTimeOff"
    )
    Page<ProfessionalTimeOff> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professionalTimeOff from ProfessionalTimeOff professionalTimeOff left join fetch professionalTimeOff.booking left join fetch professionalTimeOff.professional"
    )
    List<ProfessionalTimeOff> findAllWithToOneRelationships();

    @Query(
        "select professionalTimeOff from ProfessionalTimeOff professionalTimeOff left join fetch professionalTimeOff.booking left join fetch professionalTimeOff.professional where professionalTimeOff.id =:id"
    )
    Optional<ProfessionalTimeOff> findOneWithToOneRelationships(@Param("id") Long id);
}

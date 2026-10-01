package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalSkill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalSkill entity.
 */
@Repository
public interface ProfessionalSkillRepository extends JpaRepository<ProfessionalSkill, Long> {
    default Optional<ProfessionalSkill> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalSkill> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalSkill> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalSkill from ProfessionalSkill professionalSkill left join fetch professionalSkill.service left join fetch professionalSkill.professional",
        countQuery = "select count(professionalSkill) from ProfessionalSkill professionalSkill"
    )
    Page<ProfessionalSkill> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professionalSkill from ProfessionalSkill professionalSkill left join fetch professionalSkill.service left join fetch professionalSkill.professional"
    )
    List<ProfessionalSkill> findAllWithToOneRelationships();

    @Query(
        "select professionalSkill from ProfessionalSkill professionalSkill left join fetch professionalSkill.service left join fetch professionalSkill.professional where professionalSkill.id =:id"
    )
    Optional<ProfessionalSkill> findOneWithToOneRelationships(@Param("id") Long id);
}

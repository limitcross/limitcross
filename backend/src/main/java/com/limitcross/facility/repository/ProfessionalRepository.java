package com.limitcross.facility.repository;

import com.limitcross.facility.domain.Professional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Professional entity.
 *
 * When extending this class, extend ProfessionalRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ProfessionalRepository
    extends ProfessionalRepositoryWithBagRelationships, JpaRepository<Professional, Long>, JpaSpecificationExecutor<Professional> {
    default Optional<Professional> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Professional> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Professional> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select professional from Professional professional left join fetch professional.user left join fetch professional.homeCity left join fetch professional.tier",
        countQuery = "select count(professional) from Professional professional"
    )
    Page<Professional> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professional from Professional professional left join fetch professional.user left join fetch professional.homeCity left join fetch professional.tier"
    )
    List<Professional> findAllWithToOneRelationships();

    @Query(
        "select professional from Professional professional left join fetch professional.user left join fetch professional.homeCity left join fetch professional.tier where professional.id =:id"
    )
    Optional<Professional> findOneWithToOneRelationships(@Param("id") Long id);
}

package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CancellationPolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CancellationPolicy entity.
 */
@Repository
public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Long> {
    default Optional<CancellationPolicy> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CancellationPolicy> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CancellationPolicy> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cancellationPolicy from CancellationPolicy cancellationPolicy left join fetch cancellationPolicy.category left join fetch cancellationPolicy.service",
        countQuery = "select count(cancellationPolicy) from CancellationPolicy cancellationPolicy"
    )
    Page<CancellationPolicy> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cancellationPolicy from CancellationPolicy cancellationPolicy left join fetch cancellationPolicy.category left join fetch cancellationPolicy.service"
    )
    List<CancellationPolicy> findAllWithToOneRelationships();

    @Query(
        "select cancellationPolicy from CancellationPolicy cancellationPolicy left join fetch cancellationPolicy.category left join fetch cancellationPolicy.service where cancellationPolicy.id =:id"
    )
    Optional<CancellationPolicy> findOneWithToOneRelationships(@Param("id") Long id);
}

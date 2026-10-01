package com.limitcross.facility.repository;

import com.limitcross.facility.domain.Payout;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Payout entity.
 */
@Repository
public interface PayoutRepository extends JpaRepository<Payout, Long> {
    default Optional<Payout> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Payout> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Payout> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select payout from Payout payout left join fetch payout.professional",
        countQuery = "select count(payout) from Payout payout"
    )
    Page<Payout> findAllWithToOneRelationships(Pageable pageable);

    @Query("select payout from Payout payout left join fetch payout.professional")
    List<Payout> findAllWithToOneRelationships();

    @Query("select payout from Payout payout left join fetch payout.professional where payout.id =:id")
    Optional<Payout> findOneWithToOneRelationships(@Param("id") Long id);
}

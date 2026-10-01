package com.limitcross.facility.repository;

import com.limitcross.facility.domain.WarrantyClaim;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WarrantyClaim entity.
 */
@Repository
public interface WarrantyClaimRepository extends JpaRepository<WarrantyClaim, Long> {
    @Query("select warrantyClaim from WarrantyClaim warrantyClaim where warrantyClaim.customer.login = ?#{authentication.name}")
    List<WarrantyClaim> findByCustomerIsCurrentUser();

    default Optional<WarrantyClaim> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<WarrantyClaim> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<WarrantyClaim> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select warrantyClaim from WarrantyClaim warrantyClaim left join fetch warrantyClaim.customer left join fetch warrantyClaim.booking left join fetch warrantyClaim.redoBooking",
        countQuery = "select count(warrantyClaim) from WarrantyClaim warrantyClaim"
    )
    Page<WarrantyClaim> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select warrantyClaim from WarrantyClaim warrantyClaim left join fetch warrantyClaim.customer left join fetch warrantyClaim.booking left join fetch warrantyClaim.redoBooking"
    )
    List<WarrantyClaim> findAllWithToOneRelationships();

    @Query(
        "select warrantyClaim from WarrantyClaim warrantyClaim left join fetch warrantyClaim.customer left join fetch warrantyClaim.booking left join fetch warrantyClaim.redoBooking where warrantyClaim.id =:id"
    )
    Optional<WarrantyClaim> findOneWithToOneRelationships(@Param("id") Long id);
}

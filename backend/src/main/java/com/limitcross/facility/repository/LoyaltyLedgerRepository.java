package com.limitcross.facility.repository;

import com.limitcross.facility.domain.LoyaltyLedger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoyaltyLedger entity.
 */
@Repository
public interface LoyaltyLedgerRepository extends JpaRepository<LoyaltyLedger, Long> {
    @Query("select loyaltyLedger from LoyaltyLedger loyaltyLedger where loyaltyLedger.user.login = ?#{authentication.name}")
    List<LoyaltyLedger> findByUserIsCurrentUser();

    default Optional<LoyaltyLedger> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LoyaltyLedger> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LoyaltyLedger> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select loyaltyLedger from LoyaltyLedger loyaltyLedger left join fetch loyaltyLedger.user left join fetch loyaltyLedger.booking",
        countQuery = "select count(loyaltyLedger) from LoyaltyLedger loyaltyLedger"
    )
    Page<LoyaltyLedger> findAllWithToOneRelationships(Pageable pageable);

    @Query("select loyaltyLedger from LoyaltyLedger loyaltyLedger left join fetch loyaltyLedger.user left join fetch loyaltyLedger.booking")
    List<LoyaltyLedger> findAllWithToOneRelationships();

    @Query(
        "select loyaltyLedger from LoyaltyLedger loyaltyLedger left join fetch loyaltyLedger.user left join fetch loyaltyLedger.booking where loyaltyLedger.id =:id"
    )
    Optional<LoyaltyLedger> findOneWithToOneRelationships(@Param("id") Long id);
}

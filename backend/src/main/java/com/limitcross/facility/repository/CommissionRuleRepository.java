package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CommissionRule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommissionRule entity.
 */
@Repository
public interface CommissionRuleRepository extends JpaRepository<CommissionRule, Long> {
    default Optional<CommissionRule> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CommissionRule> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CommissionRule> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select commissionRule from CommissionRule commissionRule left join fetch commissionRule.service left join fetch commissionRule.category left join fetch commissionRule.city left join fetch commissionRule.tier",
        countQuery = "select count(commissionRule) from CommissionRule commissionRule"
    )
    Page<CommissionRule> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select commissionRule from CommissionRule commissionRule left join fetch commissionRule.service left join fetch commissionRule.category left join fetch commissionRule.city left join fetch commissionRule.tier"
    )
    List<CommissionRule> findAllWithToOneRelationships();

    @Query(
        "select commissionRule from CommissionRule commissionRule left join fetch commissionRule.service left join fetch commissionRule.category left join fetch commissionRule.city left join fetch commissionRule.tier where commissionRule.id =:id"
    )
    Optional<CommissionRule> findOneWithToOneRelationships(@Param("id") Long id);
}

package com.limitcross.facility.repository;

import com.limitcross.facility.domain.IncentiveRule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IncentiveRule entity.
 */
@Repository
public interface IncentiveRuleRepository extends JpaRepository<IncentiveRule, Long> {
    default Optional<IncentiveRule> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<IncentiveRule> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<IncentiveRule> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select incentiveRule from IncentiveRule incentiveRule left join fetch incentiveRule.city",
        countQuery = "select count(incentiveRule) from IncentiveRule incentiveRule"
    )
    Page<IncentiveRule> findAllWithToOneRelationships(Pageable pageable);

    @Query("select incentiveRule from IncentiveRule incentiveRule left join fetch incentiveRule.city")
    List<IncentiveRule> findAllWithToOneRelationships();

    @Query("select incentiveRule from IncentiveRule incentiveRule left join fetch incentiveRule.city where incentiveRule.id =:id")
    Optional<IncentiveRule> findOneWithToOneRelationships(@Param("id") Long id);
}

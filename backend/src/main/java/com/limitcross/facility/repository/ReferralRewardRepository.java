package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ReferralReward;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReferralReward entity.
 */
@Repository
public interface ReferralRewardRepository extends JpaRepository<ReferralReward, Long> {
    @Query("select referralReward from ReferralReward referralReward where referralReward.referrer.login = ?#{authentication.name}")
    List<ReferralReward> findByReferrerIsCurrentUser();

    @Query("select referralReward from ReferralReward referralReward where referralReward.referee.login = ?#{authentication.name}")
    List<ReferralReward> findByRefereeIsCurrentUser();

    default Optional<ReferralReward> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReferralReward> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReferralReward> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select referralReward from ReferralReward referralReward left join fetch referralReward.referrer left join fetch referralReward.referee left join fetch referralReward.triggerBooking",
        countQuery = "select count(referralReward) from ReferralReward referralReward"
    )
    Page<ReferralReward> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select referralReward from ReferralReward referralReward left join fetch referralReward.referrer left join fetch referralReward.referee left join fetch referralReward.triggerBooking"
    )
    List<ReferralReward> findAllWithToOneRelationships();

    @Query(
        "select referralReward from ReferralReward referralReward left join fetch referralReward.referrer left join fetch referralReward.referee left join fetch referralReward.triggerBooking where referralReward.id =:id"
    )
    Optional<ReferralReward> findOneWithToOneRelationships(@Param("id") Long id);
}

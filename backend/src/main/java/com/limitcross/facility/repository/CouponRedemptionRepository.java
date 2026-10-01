package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CouponRedemption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CouponRedemption entity.
 */
@Repository
public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {
    @Query("select couponRedemption from CouponRedemption couponRedemption where couponRedemption.user.login = ?#{authentication.name}")
    List<CouponRedemption> findByUserIsCurrentUser();

    default Optional<CouponRedemption> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CouponRedemption> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CouponRedemption> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select couponRedemption from CouponRedemption couponRedemption left join fetch couponRedemption.booking left join fetch couponRedemption.user left join fetch couponRedemption.coupon",
        countQuery = "select count(couponRedemption) from CouponRedemption couponRedemption"
    )
    Page<CouponRedemption> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select couponRedemption from CouponRedemption couponRedemption left join fetch couponRedemption.booking left join fetch couponRedemption.user left join fetch couponRedemption.coupon"
    )
    List<CouponRedemption> findAllWithToOneRelationships();

    @Query(
        "select couponRedemption from CouponRedemption couponRedemption left join fetch couponRedemption.booking left join fetch couponRedemption.user left join fetch couponRedemption.coupon where couponRedemption.id =:id"
    )
    Optional<CouponRedemption> findOneWithToOneRelationships(@Param("id") Long id);
}

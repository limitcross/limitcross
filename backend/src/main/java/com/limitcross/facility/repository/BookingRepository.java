package com.limitcross.facility.repository;

import com.limitcross.facility.domain.Booking;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Booking entity.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    @Query("select booking from Booking booking where booking.customer.login = ?#{authentication.name}")
    List<Booking> findByCustomerIsCurrentUser();

    List<Booking> findAllByCustomer_LoginOrderByScheduledStartDesc(String login);

    Optional<Booking> findOneByPublicIdAndCustomer_Login(UUID publicId, String login);

    default Optional<Booking> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Booking> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Booking> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select booking from Booking booking left join fetch booking.customer left join fetch booking.service left join fetch booking.city left join fetch booking.address left join fetch booking.professional left join fetch booking.coupon",
        countQuery = "select count(booking) from Booking booking"
    )
    Page<Booking> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select booking from Booking booking left join fetch booking.customer left join fetch booking.service left join fetch booking.city left join fetch booking.address left join fetch booking.professional left join fetch booking.coupon"
    )
    List<Booking> findAllWithToOneRelationships();

    @Query(
        "select booking from Booking booking left join fetch booking.customer left join fetch booking.service left join fetch booking.city left join fetch booking.address left join fetch booking.professional left join fetch booking.coupon where booking.id =:id"
    )
    Optional<Booking> findOneWithToOneRelationships(@Param("id") Long id);
}

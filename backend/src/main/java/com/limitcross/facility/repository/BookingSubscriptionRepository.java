package com.limitcross.facility.repository;

import com.limitcross.facility.domain.BookingSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingSubscription entity.
 */
@Repository
public interface BookingSubscriptionRepository extends JpaRepository<BookingSubscription, Long> {
    @Query(
        "select bookingSubscription from BookingSubscription bookingSubscription where bookingSubscription.customer.login = ?#{authentication.name}"
    )
    List<BookingSubscription> findByCustomerIsCurrentUser();

    default Optional<BookingSubscription> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookingSubscription> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookingSubscription> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookingSubscription from BookingSubscription bookingSubscription left join fetch bookingSubscription.customer left join fetch bookingSubscription.service left join fetch bookingSubscription.servicePackage left join fetch bookingSubscription.address left join fetch bookingSubscription.preferredProfessional",
        countQuery = "select count(bookingSubscription) from BookingSubscription bookingSubscription"
    )
    Page<BookingSubscription> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select bookingSubscription from BookingSubscription bookingSubscription left join fetch bookingSubscription.customer left join fetch bookingSubscription.service left join fetch bookingSubscription.servicePackage left join fetch bookingSubscription.address left join fetch bookingSubscription.preferredProfessional"
    )
    List<BookingSubscription> findAllWithToOneRelationships();

    @Query(
        "select bookingSubscription from BookingSubscription bookingSubscription left join fetch bookingSubscription.customer left join fetch bookingSubscription.service left join fetch bookingSubscription.servicePackage left join fetch bookingSubscription.address left join fetch bookingSubscription.preferredProfessional where bookingSubscription.id =:id"
    )
    Optional<BookingSubscription> findOneWithToOneRelationships(@Param("id") Long id);
}

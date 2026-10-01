package com.limitcross.facility.repository;

import com.limitcross.facility.domain.BookingReschedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingReschedule entity.
 */
@Repository
public interface BookingRescheduleRepository extends JpaRepository<BookingReschedule, Long> {
    default Optional<BookingReschedule> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookingReschedule> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookingReschedule> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookingReschedule from BookingReschedule bookingReschedule left join fetch bookingReschedule.booking",
        countQuery = "select count(bookingReschedule) from BookingReschedule bookingReschedule"
    )
    Page<BookingReschedule> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bookingReschedule from BookingReschedule bookingReschedule left join fetch bookingReschedule.booking")
    List<BookingReschedule> findAllWithToOneRelationships();

    @Query(
        "select bookingReschedule from BookingReschedule bookingReschedule left join fetch bookingReschedule.booking where bookingReschedule.id =:id"
    )
    Optional<BookingReschedule> findOneWithToOneRelationships(@Param("id") Long id);
}

package com.limitcross.facility.repository;

import com.limitcross.facility.domain.BookingAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingAssignment entity.
 */
@Repository
public interface BookingAssignmentRepository extends JpaRepository<BookingAssignment, Long> {
    default Optional<BookingAssignment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookingAssignment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookingAssignment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookingAssignment from BookingAssignment bookingAssignment left join fetch bookingAssignment.professional left join fetch bookingAssignment.booking",
        countQuery = "select count(bookingAssignment) from BookingAssignment bookingAssignment"
    )
    Page<BookingAssignment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select bookingAssignment from BookingAssignment bookingAssignment left join fetch bookingAssignment.professional left join fetch bookingAssignment.booking"
    )
    List<BookingAssignment> findAllWithToOneRelationships();

    @Query(
        "select bookingAssignment from BookingAssignment bookingAssignment left join fetch bookingAssignment.professional left join fetch bookingAssignment.booking where bookingAssignment.id =:id"
    )
    Optional<BookingAssignment> findOneWithToOneRelationships(@Param("id") Long id);
}

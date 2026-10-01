package com.limitcross.facility.repository;

import com.limitcross.facility.domain.BookingQuote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingQuote entity.
 */
@Repository
public interface BookingQuoteRepository extends JpaRepository<BookingQuote, Long> {
    default Optional<BookingQuote> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookingQuote> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookingQuote> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookingQuote from BookingQuote bookingQuote left join fetch bookingQuote.professional left join fetch bookingQuote.booking",
        countQuery = "select count(bookingQuote) from BookingQuote bookingQuote"
    )
    Page<BookingQuote> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select bookingQuote from BookingQuote bookingQuote left join fetch bookingQuote.professional left join fetch bookingQuote.booking"
    )
    List<BookingQuote> findAllWithToOneRelationships();

    @Query(
        "select bookingQuote from BookingQuote bookingQuote left join fetch bookingQuote.professional left join fetch bookingQuote.booking where bookingQuote.id =:id"
    )
    Optional<BookingQuote> findOneWithToOneRelationships(@Param("id") Long id);
}

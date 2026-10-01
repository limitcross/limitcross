package com.limitcross.facility.repository;

import com.limitcross.facility.domain.BookingStatusHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingStatusHistory entity.
 */
@Repository
public interface BookingStatusHistoryRepository extends JpaRepository<BookingStatusHistory, Long> {
    default Optional<BookingStatusHistory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookingStatusHistory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookingStatusHistory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookingStatusHistory from BookingStatusHistory bookingStatusHistory left join fetch bookingStatusHistory.booking",
        countQuery = "select count(bookingStatusHistory) from BookingStatusHistory bookingStatusHistory"
    )
    Page<BookingStatusHistory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bookingStatusHistory from BookingStatusHistory bookingStatusHistory left join fetch bookingStatusHistory.booking")
    List<BookingStatusHistory> findAllWithToOneRelationships();

    @Query(
        "select bookingStatusHistory from BookingStatusHistory bookingStatusHistory left join fetch bookingStatusHistory.booking where bookingStatusHistory.id =:id"
    )
    Optional<BookingStatusHistory> findOneWithToOneRelationships(@Param("id") Long id);
}

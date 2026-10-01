package com.limitcross.facility.repository;

import com.limitcross.facility.domain.BookingMedia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingMedia entity.
 */
@Repository
public interface BookingMediaRepository extends JpaRepository<BookingMedia, Long> {
    @Query("select bookingMedia from BookingMedia bookingMedia where bookingMedia.uploadedBy.login = ?#{authentication.name}")
    List<BookingMedia> findByUploadedByIsCurrentUser();

    default Optional<BookingMedia> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BookingMedia> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BookingMedia> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bookingMedia from BookingMedia bookingMedia left join fetch bookingMedia.uploadedBy left join fetch bookingMedia.booking",
        countQuery = "select count(bookingMedia) from BookingMedia bookingMedia"
    )
    Page<BookingMedia> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select bookingMedia from BookingMedia bookingMedia left join fetch bookingMedia.uploadedBy left join fetch bookingMedia.booking"
    )
    List<BookingMedia> findAllWithToOneRelationships();

    @Query(
        "select bookingMedia from BookingMedia bookingMedia left join fetch bookingMedia.uploadedBy left join fetch bookingMedia.booking where bookingMedia.id =:id"
    )
    Optional<BookingMedia> findOneWithToOneRelationships(@Param("id") Long id);
}

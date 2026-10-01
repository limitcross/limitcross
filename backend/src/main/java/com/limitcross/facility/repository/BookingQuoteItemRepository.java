package com.limitcross.facility.repository;

import com.limitcross.facility.domain.BookingQuoteItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingQuoteItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingQuoteItemRepository extends JpaRepository<BookingQuoteItem, Long> {}

package com.limitcross.facility.repository;

import com.limitcross.facility.domain.InvoiceSequence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceSequence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceSequenceRepository extends JpaRepository<InvoiceSequence, Long> {}

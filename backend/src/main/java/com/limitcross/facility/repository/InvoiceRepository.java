package com.limitcross.facility.repository;

import com.limitcross.facility.domain.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("select invoice from Invoice invoice where invoice.customer.login = ?#{authentication.name}")
    List<Invoice> findByCustomerIsCurrentUser();

    default Optional<Invoice> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Invoice> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Invoice> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select invoice from Invoice invoice left join fetch invoice.customer left join fetch invoice.booking",
        countQuery = "select count(invoice) from Invoice invoice"
    )
    Page<Invoice> findAllWithToOneRelationships(Pageable pageable);

    @Query("select invoice from Invoice invoice left join fetch invoice.customer left join fetch invoice.booking")
    List<Invoice> findAllWithToOneRelationships();

    @Query("select invoice from Invoice invoice left join fetch invoice.customer left join fetch invoice.booking where invoice.id =:id")
    Optional<Invoice> findOneWithToOneRelationships(@Param("id") Long id);
}

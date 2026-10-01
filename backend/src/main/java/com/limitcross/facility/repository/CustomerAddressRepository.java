package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CustomerAddress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerAddress entity.
 */
@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long>, JpaSpecificationExecutor<CustomerAddress> {
    @Query("select customerAddress from CustomerAddress customerAddress where customerAddress.customer.login = ?#{authentication.name}")
    List<CustomerAddress> findByCustomerIsCurrentUser();

    default Optional<CustomerAddress> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustomerAddress> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustomerAddress> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select customerAddress from CustomerAddress customerAddress left join fetch customerAddress.customer left join fetch customerAddress.city",
        countQuery = "select count(customerAddress) from CustomerAddress customerAddress"
    )
    Page<CustomerAddress> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select customerAddress from CustomerAddress customerAddress left join fetch customerAddress.customer left join fetch customerAddress.city"
    )
    List<CustomerAddress> findAllWithToOneRelationships();

    @Query(
        "select customerAddress from CustomerAddress customerAddress left join fetch customerAddress.customer left join fetch customerAddress.city where customerAddress.id =:id"
    )
    Optional<CustomerAddress> findOneWithToOneRelationships(@Param("id") Long id);
}

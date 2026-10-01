package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CustomerWalletTxn;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerWalletTxn entity.
 */
@Repository
public interface CustomerWalletTxnRepository extends JpaRepository<CustomerWalletTxn, Long> {
    @Query("select customerWalletTxn from CustomerWalletTxn customerWalletTxn where customerWalletTxn.user.login = ?#{authentication.name}")
    List<CustomerWalletTxn> findByUserIsCurrentUser();

    default Optional<CustomerWalletTxn> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustomerWalletTxn> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustomerWalletTxn> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select customerWalletTxn from CustomerWalletTxn customerWalletTxn left join fetch customerWalletTxn.user left join fetch customerWalletTxn.booking",
        countQuery = "select count(customerWalletTxn) from CustomerWalletTxn customerWalletTxn"
    )
    Page<CustomerWalletTxn> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select customerWalletTxn from CustomerWalletTxn customerWalletTxn left join fetch customerWalletTxn.user left join fetch customerWalletTxn.booking"
    )
    List<CustomerWalletTxn> findAllWithToOneRelationships();

    @Query(
        "select customerWalletTxn from CustomerWalletTxn customerWalletTxn left join fetch customerWalletTxn.user left join fetch customerWalletTxn.booking where customerWalletTxn.id =:id"
    )
    Optional<CustomerWalletTxn> findOneWithToOneRelationships(@Param("id") Long id);
}

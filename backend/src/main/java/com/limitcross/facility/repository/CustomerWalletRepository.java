package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CustomerWallet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerWallet entity.
 */
@Repository
public interface CustomerWalletRepository extends JpaRepository<CustomerWallet, Long> {
    default Optional<CustomerWallet> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CustomerWallet> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CustomerWallet> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select customerWallet from CustomerWallet customerWallet left join fetch customerWallet.user",
        countQuery = "select count(customerWallet) from CustomerWallet customerWallet"
    )
    Page<CustomerWallet> findAllWithToOneRelationships(Pageable pageable);

    @Query("select customerWallet from CustomerWallet customerWallet left join fetch customerWallet.user")
    List<CustomerWallet> findAllWithToOneRelationships();

    @Query("select customerWallet from CustomerWallet customerWallet left join fetch customerWallet.user where customerWallet.id =:id")
    Optional<CustomerWallet> findOneWithToOneRelationships(@Param("id") Long id);
}

package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalWallet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalWallet entity.
 */
@Repository
public interface ProfessionalWalletRepository extends JpaRepository<ProfessionalWallet, Long> {
    default Optional<ProfessionalWallet> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalWallet> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalWallet> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalWallet from ProfessionalWallet professionalWallet left join fetch professionalWallet.professional",
        countQuery = "select count(professionalWallet) from ProfessionalWallet professionalWallet"
    )
    Page<ProfessionalWallet> findAllWithToOneRelationships(Pageable pageable);

    @Query("select professionalWallet from ProfessionalWallet professionalWallet left join fetch professionalWallet.professional")
    List<ProfessionalWallet> findAllWithToOneRelationships();

    @Query(
        "select professionalWallet from ProfessionalWallet professionalWallet left join fetch professionalWallet.professional where professionalWallet.id =:id"
    )
    Optional<ProfessionalWallet> findOneWithToOneRelationships(@Param("id") Long id);
}

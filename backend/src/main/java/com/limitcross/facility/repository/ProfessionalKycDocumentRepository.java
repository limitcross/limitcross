package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ProfessionalKycDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalKycDocument entity.
 */
@Repository
public interface ProfessionalKycDocumentRepository extends JpaRepository<ProfessionalKycDocument, Long> {
    @Query(
        "select professionalKycDocument from ProfessionalKycDocument professionalKycDocument where professionalKycDocument.reviewer.login = ?#{authentication.name}"
    )
    List<ProfessionalKycDocument> findByReviewerIsCurrentUser();

    default Optional<ProfessionalKycDocument> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProfessionalKycDocument> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProfessionalKycDocument> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select professionalKycDocument from ProfessionalKycDocument professionalKycDocument left join fetch professionalKycDocument.reviewer left join fetch professionalKycDocument.professional",
        countQuery = "select count(professionalKycDocument) from ProfessionalKycDocument professionalKycDocument"
    )
    Page<ProfessionalKycDocument> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select professionalKycDocument from ProfessionalKycDocument professionalKycDocument left join fetch professionalKycDocument.reviewer left join fetch professionalKycDocument.professional"
    )
    List<ProfessionalKycDocument> findAllWithToOneRelationships();

    @Query(
        "select professionalKycDocument from ProfessionalKycDocument professionalKycDocument left join fetch professionalKycDocument.reviewer left join fetch professionalKycDocument.professional where professionalKycDocument.id =:id"
    )
    Optional<ProfessionalKycDocument> findOneWithToOneRelationships(@Param("id") Long id);
}

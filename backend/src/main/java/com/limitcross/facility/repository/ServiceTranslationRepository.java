package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ServiceTranslation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceTranslation entity.
 */
@Repository
public interface ServiceTranslationRepository extends JpaRepository<ServiceTranslation, Long> {
    default Optional<ServiceTranslation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServiceTranslation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServiceTranslation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select serviceTranslation from ServiceTranslation serviceTranslation left join fetch serviceTranslation.service",
        countQuery = "select count(serviceTranslation) from ServiceTranslation serviceTranslation"
    )
    Page<ServiceTranslation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select serviceTranslation from ServiceTranslation serviceTranslation left join fetch serviceTranslation.service")
    List<ServiceTranslation> findAllWithToOneRelationships();

    @Query(
        "select serviceTranslation from ServiceTranslation serviceTranslation left join fetch serviceTranslation.service where serviceTranslation.id =:id"
    )
    Optional<ServiceTranslation> findOneWithToOneRelationships(@Param("id") Long id);
}

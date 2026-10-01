package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ServicePackage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServicePackage entity.
 */
@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {
    default Optional<ServicePackage> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServicePackage> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServicePackage> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select servicePackage from ServicePackage servicePackage left join fetch servicePackage.service",
        countQuery = "select count(servicePackage) from ServicePackage servicePackage"
    )
    Page<ServicePackage> findAllWithToOneRelationships(Pageable pageable);

    @Query("select servicePackage from ServicePackage servicePackage left join fetch servicePackage.service")
    List<ServicePackage> findAllWithToOneRelationships();

    @Query("select servicePackage from ServicePackage servicePackage left join fetch servicePackage.service where servicePackage.id =:id")
    Optional<ServicePackage> findOneWithToOneRelationships(@Param("id") Long id);
}

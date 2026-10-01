package com.limitcross.facility.repository;

import com.limitcross.facility.domain.FacilityService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityService entity.
 */
@Repository
public interface FacilityServiceRepository extends JpaRepository<FacilityService, Long>, JpaSpecificationExecutor<FacilityService> {
    default Optional<FacilityService> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FacilityService> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FacilityService> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select facilityService from FacilityService facilityService left join fetch facilityService.category",
        countQuery = "select count(facilityService) from FacilityService facilityService"
    )
    Page<FacilityService> findAllWithToOneRelationships(Pageable pageable);

    @Query("select facilityService from FacilityService facilityService left join fetch facilityService.category")
    List<FacilityService> findAllWithToOneRelationships();

    @Query(
        "select distinct facilityService from FacilityService facilityService " +
        "left join fetch facilityService.category left join fetch facilityService.servicePackages " +
        "where facilityService.code = :code"
    )
    Optional<FacilityService> findWithPackagesByCode(@Param("code") String code);

    @Query(
        "select facilityService from FacilityService facilityService left join fetch facilityService.category where facilityService.id =:id"
    )
    Optional<FacilityService> findOneWithToOneRelationships(@Param("id") Long id);
}

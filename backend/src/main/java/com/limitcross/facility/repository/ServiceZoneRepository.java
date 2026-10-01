package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ServiceZone;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceZone entity.
 */
@Repository
public interface ServiceZoneRepository extends JpaRepository<ServiceZone, Long> {
    default Optional<ServiceZone> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServiceZone> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServiceZone> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select serviceZone from ServiceZone serviceZone left join fetch serviceZone.city",
        countQuery = "select count(serviceZone) from ServiceZone serviceZone"
    )
    Page<ServiceZone> findAllWithToOneRelationships(Pageable pageable);

    @Query("select serviceZone from ServiceZone serviceZone left join fetch serviceZone.city")
    List<ServiceZone> findAllWithToOneRelationships();

    @Query("select serviceZone from ServiceZone serviceZone left join fetch serviceZone.city where serviceZone.id =:id")
    Optional<ServiceZone> findOneWithToOneRelationships(@Param("id") Long id);
}

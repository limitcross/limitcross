package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ServiceAddon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceAddon entity.
 */
@Repository
public interface ServiceAddonRepository extends JpaRepository<ServiceAddon, Long> {
    default Optional<ServiceAddon> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServiceAddon> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServiceAddon> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select serviceAddon from ServiceAddon serviceAddon left join fetch serviceAddon.service",
        countQuery = "select count(serviceAddon) from ServiceAddon serviceAddon"
    )
    Page<ServiceAddon> findAllWithToOneRelationships(Pageable pageable);

    @Query("select serviceAddon from ServiceAddon serviceAddon left join fetch serviceAddon.service")
    List<ServiceAddon> findAllWithToOneRelationships();

    @Query("select serviceAddon from ServiceAddon serviceAddon left join fetch serviceAddon.service where serviceAddon.id =:id")
    Optional<ServiceAddon> findOneWithToOneRelationships(@Param("id") Long id);
}

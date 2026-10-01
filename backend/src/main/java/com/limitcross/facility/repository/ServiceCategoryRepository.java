package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ServiceCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceCategory entity.
 */
@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    default Optional<ServiceCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ServiceCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ServiceCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select serviceCategory from ServiceCategory serviceCategory left join fetch serviceCategory.parent",
        countQuery = "select count(serviceCategory) from ServiceCategory serviceCategory"
    )
    Page<ServiceCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select serviceCategory from ServiceCategory serviceCategory left join fetch serviceCategory.parent")
    List<ServiceCategory> findAllWithToOneRelationships();

    @Query(
        "select serviceCategory from ServiceCategory serviceCategory left join fetch serviceCategory.parent where serviceCategory.id =:id"
    )
    Optional<ServiceCategory> findOneWithToOneRelationships(@Param("id") Long id);
}

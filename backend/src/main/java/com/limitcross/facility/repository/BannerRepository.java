package com.limitcross.facility.repository;

import com.limitcross.facility.domain.Banner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Banner entity.
 */
@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    default Optional<Banner> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Banner> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Banner> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select banner from Banner banner left join fetch banner.city", countQuery = "select count(banner) from Banner banner")
    Page<Banner> findAllWithToOneRelationships(Pageable pageable);

    @Query("select banner from Banner banner left join fetch banner.city")
    List<Banner> findAllWithToOneRelationships();

    @Query("select banner from Banner banner left join fetch banner.city where banner.id =:id")
    Optional<Banner> findOneWithToOneRelationships(@Param("id") Long id);
}

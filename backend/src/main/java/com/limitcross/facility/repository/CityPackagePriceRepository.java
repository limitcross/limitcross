package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CityPackagePrice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CityPackagePrice entity.
 */
@Repository
public interface CityPackagePriceRepository extends JpaRepository<CityPackagePrice, Long> {
    default Optional<CityPackagePrice> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CityPackagePrice> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CityPackagePrice> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cityPackagePrice from CityPackagePrice cityPackagePrice left join fetch cityPackagePrice.servicePackage left join fetch cityPackagePrice.city",
        countQuery = "select count(cityPackagePrice) from CityPackagePrice cityPackagePrice"
    )
    Page<CityPackagePrice> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cityPackagePrice from CityPackagePrice cityPackagePrice left join fetch cityPackagePrice.servicePackage left join fetch cityPackagePrice.city"
    )
    List<CityPackagePrice> findAllWithToOneRelationships();

    @Query(
        "select cityPackagePrice from CityPackagePrice cityPackagePrice left join fetch cityPackagePrice.servicePackage left join fetch cityPackagePrice.city where cityPackagePrice.id =:id"
    )
    Optional<CityPackagePrice> findOneWithToOneRelationships(@Param("id") Long id);
}

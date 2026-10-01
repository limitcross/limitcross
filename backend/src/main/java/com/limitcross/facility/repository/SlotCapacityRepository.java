package com.limitcross.facility.repository;

import com.limitcross.facility.domain.SlotCapacity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SlotCapacity entity.
 */
@Repository
public interface SlotCapacityRepository extends JpaRepository<SlotCapacity, Long> {
    default Optional<SlotCapacity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SlotCapacity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SlotCapacity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select slotCapacity from SlotCapacity slotCapacity left join fetch slotCapacity.zone left join fetch slotCapacity.category",
        countQuery = "select count(slotCapacity) from SlotCapacity slotCapacity"
    )
    Page<SlotCapacity> findAllWithToOneRelationships(Pageable pageable);

    @Query("select slotCapacity from SlotCapacity slotCapacity left join fetch slotCapacity.zone left join fetch slotCapacity.category")
    List<SlotCapacity> findAllWithToOneRelationships();

    @Query(
        "select slotCapacity from SlotCapacity slotCapacity left join fetch slotCapacity.zone left join fetch slotCapacity.category where slotCapacity.id =:id"
    )
    Optional<SlotCapacity> findOneWithToOneRelationships(@Param("id") Long id);
}

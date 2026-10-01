package com.limitcross.facility.repository;

import com.limitcross.facility.domain.SlotHold;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SlotHold entity.
 */
@Repository
public interface SlotHoldRepository extends JpaRepository<SlotHold, Long> {
    @Query("select slotHold from SlotHold slotHold where slotHold.user.login = ?#{authentication.name}")
    List<SlotHold> findByUserIsCurrentUser();

    default Optional<SlotHold> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SlotHold> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SlotHold> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select slotHold from SlotHold slotHold left join fetch slotHold.user left join fetch slotHold.booking",
        countQuery = "select count(slotHold) from SlotHold slotHold"
    )
    Page<SlotHold> findAllWithToOneRelationships(Pageable pageable);

    @Query("select slotHold from SlotHold slotHold left join fetch slotHold.user left join fetch slotHold.booking")
    List<SlotHold> findAllWithToOneRelationships();

    @Query("select slotHold from SlotHold slotHold left join fetch slotHold.user left join fetch slotHold.booking where slotHold.id =:id")
    Optional<SlotHold> findOneWithToOneRelationships(@Param("id") Long id);
}

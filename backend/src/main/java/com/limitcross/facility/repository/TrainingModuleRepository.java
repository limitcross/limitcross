package com.limitcross.facility.repository;

import com.limitcross.facility.domain.TrainingModule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrainingModule entity.
 */
@Repository
public interface TrainingModuleRepository extends JpaRepository<TrainingModule, Long> {
    default Optional<TrainingModule> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TrainingModule> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TrainingModule> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select trainingModule from TrainingModule trainingModule left join fetch trainingModule.service",
        countQuery = "select count(trainingModule) from TrainingModule trainingModule"
    )
    Page<TrainingModule> findAllWithToOneRelationships(Pageable pageable);

    @Query("select trainingModule from TrainingModule trainingModule left join fetch trainingModule.service")
    List<TrainingModule> findAllWithToOneRelationships();

    @Query("select trainingModule from TrainingModule trainingModule left join fetch trainingModule.service where trainingModule.id =:id")
    Optional<TrainingModule> findOneWithToOneRelationships(@Param("id") Long id);
}

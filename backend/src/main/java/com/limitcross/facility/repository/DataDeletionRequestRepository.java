package com.limitcross.facility.repository;

import com.limitcross.facility.domain.DataDeletionRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DataDeletionRequest entity.
 */
@Repository
public interface DataDeletionRequestRepository extends JpaRepository<DataDeletionRequest, Long> {
    @Query(
        "select dataDeletionRequest from DataDeletionRequest dataDeletionRequest where dataDeletionRequest.user.login = ?#{authentication.name}"
    )
    List<DataDeletionRequest> findByUserIsCurrentUser();

    default Optional<DataDeletionRequest> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DataDeletionRequest> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DataDeletionRequest> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select dataDeletionRequest from DataDeletionRequest dataDeletionRequest left join fetch dataDeletionRequest.user",
        countQuery = "select count(dataDeletionRequest) from DataDeletionRequest dataDeletionRequest"
    )
    Page<DataDeletionRequest> findAllWithToOneRelationships(Pageable pageable);

    @Query("select dataDeletionRequest from DataDeletionRequest dataDeletionRequest left join fetch dataDeletionRequest.user")
    List<DataDeletionRequest> findAllWithToOneRelationships();

    @Query(
        "select dataDeletionRequest from DataDeletionRequest dataDeletionRequest left join fetch dataDeletionRequest.user where dataDeletionRequest.id =:id"
    )
    Optional<DataDeletionRequest> findOneWithToOneRelationships(@Param("id") Long id);
}

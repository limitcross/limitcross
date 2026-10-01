package com.limitcross.facility.repository;

import com.limitcross.facility.domain.SosAlert;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SosAlert entity.
 */
@Repository
public interface SosAlertRepository extends JpaRepository<SosAlert, Long> {
    @Query("select sosAlert from SosAlert sosAlert where sosAlert.raisedBy.login = ?#{authentication.name}")
    List<SosAlert> findByRaisedByIsCurrentUser();

    @Query("select sosAlert from SosAlert sosAlert where sosAlert.handledBy.login = ?#{authentication.name}")
    List<SosAlert> findByHandledByIsCurrentUser();

    default Optional<SosAlert> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SosAlert> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SosAlert> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select sosAlert from SosAlert sosAlert left join fetch sosAlert.raisedBy left join fetch sosAlert.handledBy left join fetch sosAlert.booking",
        countQuery = "select count(sosAlert) from SosAlert sosAlert"
    )
    Page<SosAlert> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select sosAlert from SosAlert sosAlert left join fetch sosAlert.raisedBy left join fetch sosAlert.handledBy left join fetch sosAlert.booking"
    )
    List<SosAlert> findAllWithToOneRelationships();

    @Query(
        "select sosAlert from SosAlert sosAlert left join fetch sosAlert.raisedBy left join fetch sosAlert.handledBy left join fetch sosAlert.booking where sosAlert.id =:id"
    )
    Optional<SosAlert> findOneWithToOneRelationships(@Param("id") Long id);
}

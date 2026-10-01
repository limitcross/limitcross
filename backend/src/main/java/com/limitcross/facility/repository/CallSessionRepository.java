package com.limitcross.facility.repository;

import com.limitcross.facility.domain.CallSession;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CallSession entity.
 */
@Repository
public interface CallSessionRepository extends JpaRepository<CallSession, Long> {
    @Query("select callSession from CallSession callSession where callSession.caller.login = ?#{authentication.name}")
    List<CallSession> findByCallerIsCurrentUser();

    @Query("select callSession from CallSession callSession where callSession.callee.login = ?#{authentication.name}")
    List<CallSession> findByCalleeIsCurrentUser();

    default Optional<CallSession> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CallSession> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CallSession> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select callSession from CallSession callSession left join fetch callSession.caller left join fetch callSession.callee left join fetch callSession.booking",
        countQuery = "select count(callSession) from CallSession callSession"
    )
    Page<CallSession> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select callSession from CallSession callSession left join fetch callSession.caller left join fetch callSession.callee left join fetch callSession.booking"
    )
    List<CallSession> findAllWithToOneRelationships();

    @Query(
        "select callSession from CallSession callSession left join fetch callSession.caller left join fetch callSession.callee left join fetch callSession.booking where callSession.id =:id"
    )
    Optional<CallSession> findOneWithToOneRelationships(@Param("id") Long id);
}

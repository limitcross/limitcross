package com.limitcross.facility.repository;

import com.limitcross.facility.domain.SupportTicket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupportTicket entity.
 */
@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long>, JpaSpecificationExecutor<SupportTicket> {
    @Query("select supportTicket from SupportTicket supportTicket where supportTicket.user.login = ?#{authentication.name}")
    List<SupportTicket> findByUserIsCurrentUser();

    @Query("select supportTicket from SupportTicket supportTicket where supportTicket.assignedTo.login = ?#{authentication.name}")
    List<SupportTicket> findByAssignedToIsCurrentUser();

    default Optional<SupportTicket> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SupportTicket> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SupportTicket> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select supportTicket from SupportTicket supportTicket left join fetch supportTicket.user left join fetch supportTicket.assignedTo left join fetch supportTicket.booking",
        countQuery = "select count(supportTicket) from SupportTicket supportTicket"
    )
    Page<SupportTicket> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select supportTicket from SupportTicket supportTicket left join fetch supportTicket.user left join fetch supportTicket.assignedTo left join fetch supportTicket.booking"
    )
    List<SupportTicket> findAllWithToOneRelationships();

    @Query(
        "select supportTicket from SupportTicket supportTicket left join fetch supportTicket.user left join fetch supportTicket.assignedTo left join fetch supportTicket.booking where supportTicket.id =:id"
    )
    Optional<SupportTicket> findOneWithToOneRelationships(@Param("id") Long id);
}

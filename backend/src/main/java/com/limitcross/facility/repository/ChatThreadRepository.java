package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ChatThread;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatThread entity.
 */
@Repository
public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {
    @Query("select chatThread from ChatThread chatThread where chatThread.customer.login = ?#{authentication.name}")
    List<ChatThread> findByCustomerIsCurrentUser();

    default Optional<ChatThread> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ChatThread> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ChatThread> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select chatThread from ChatThread chatThread left join fetch chatThread.booking left join fetch chatThread.customer left join fetch chatThread.professional",
        countQuery = "select count(chatThread) from ChatThread chatThread"
    )
    Page<ChatThread> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select chatThread from ChatThread chatThread left join fetch chatThread.booking left join fetch chatThread.customer left join fetch chatThread.professional"
    )
    List<ChatThread> findAllWithToOneRelationships();

    @Query(
        "select chatThread from ChatThread chatThread left join fetch chatThread.booking left join fetch chatThread.customer left join fetch chatThread.professional where chatThread.id =:id"
    )
    Optional<ChatThread> findOneWithToOneRelationships(@Param("id") Long id);
}

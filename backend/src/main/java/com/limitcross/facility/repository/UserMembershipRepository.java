package com.limitcross.facility.repository;

import com.limitcross.facility.domain.UserMembership;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserMembership entity.
 */
@Repository
public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    @Query("select userMembership from UserMembership userMembership where userMembership.user.login = ?#{authentication.name}")
    List<UserMembership> findByUserIsCurrentUser();

    default Optional<UserMembership> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserMembership> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserMembership> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userMembership from UserMembership userMembership left join fetch userMembership.user left join fetch userMembership.plan",
        countQuery = "select count(userMembership) from UserMembership userMembership"
    )
    Page<UserMembership> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select userMembership from UserMembership userMembership left join fetch userMembership.user left join fetch userMembership.plan"
    )
    List<UserMembership> findAllWithToOneRelationships();

    @Query(
        "select userMembership from UserMembership userMembership left join fetch userMembership.user left join fetch userMembership.plan where userMembership.id =:id"
    )
    Optional<UserMembership> findOneWithToOneRelationships(@Param("id") Long id);
}

package com.limitcross.facility.repository;

import com.limitcross.facility.domain.UserConsent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserConsent entity.
 */
@Repository
public interface UserConsentRepository extends JpaRepository<UserConsent, Long> {
    @Query("select userConsent from UserConsent userConsent where userConsent.user.login = ?#{authentication.name}")
    List<UserConsent> findByUserIsCurrentUser();

    default Optional<UserConsent> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserConsent> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserConsent> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userConsent from UserConsent userConsent left join fetch userConsent.user",
        countQuery = "select count(userConsent) from UserConsent userConsent"
    )
    Page<UserConsent> findAllWithToOneRelationships(Pageable pageable);

    @Query("select userConsent from UserConsent userConsent left join fetch userConsent.user")
    List<UserConsent> findAllWithToOneRelationships();

    @Query("select userConsent from UserConsent userConsent left join fetch userConsent.user where userConsent.id =:id")
    Optional<UserConsent> findOneWithToOneRelationships(@Param("id") Long id);
}

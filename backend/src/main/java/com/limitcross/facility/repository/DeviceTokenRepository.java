package com.limitcross.facility.repository;

import com.limitcross.facility.domain.DeviceToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeviceToken entity.
 */
@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    @Query("select deviceToken from DeviceToken deviceToken where deviceToken.user.login = ?#{authentication.name}")
    List<DeviceToken> findByUserIsCurrentUser();

    default Optional<DeviceToken> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DeviceToken> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DeviceToken> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select deviceToken from DeviceToken deviceToken left join fetch deviceToken.user",
        countQuery = "select count(deviceToken) from DeviceToken deviceToken"
    )
    Page<DeviceToken> findAllWithToOneRelationships(Pageable pageable);

    @Query("select deviceToken from DeviceToken deviceToken left join fetch deviceToken.user")
    List<DeviceToken> findAllWithToOneRelationships();

    @Query("select deviceToken from DeviceToken deviceToken left join fetch deviceToken.user where deviceToken.id =:id")
    Optional<DeviceToken> findOneWithToOneRelationships(@Param("id") Long id);
}

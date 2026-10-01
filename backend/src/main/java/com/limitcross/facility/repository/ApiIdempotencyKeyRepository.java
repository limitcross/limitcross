package com.limitcross.facility.repository;

import com.limitcross.facility.domain.ApiIdempotencyKey;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApiIdempotencyKey entity.
 */
@Repository
public interface ApiIdempotencyKeyRepository extends JpaRepository<ApiIdempotencyKey, Long> {
    @Query("select apiIdempotencyKey from ApiIdempotencyKey apiIdempotencyKey where apiIdempotencyKey.user.login = ?#{authentication.name}")
    List<ApiIdempotencyKey> findByUserIsCurrentUser();

    default Optional<ApiIdempotencyKey> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ApiIdempotencyKey> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ApiIdempotencyKey> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select apiIdempotencyKey from ApiIdempotencyKey apiIdempotencyKey left join fetch apiIdempotencyKey.user",
        countQuery = "select count(apiIdempotencyKey) from ApiIdempotencyKey apiIdempotencyKey"
    )
    Page<ApiIdempotencyKey> findAllWithToOneRelationships(Pageable pageable);

    @Query("select apiIdempotencyKey from ApiIdempotencyKey apiIdempotencyKey left join fetch apiIdempotencyKey.user")
    List<ApiIdempotencyKey> findAllWithToOneRelationships();

    @Query(
        "select apiIdempotencyKey from ApiIdempotencyKey apiIdempotencyKey left join fetch apiIdempotencyKey.user where apiIdempotencyKey.id =:id"
    )
    Optional<ApiIdempotencyKey> findOneWithToOneRelationships(@Param("id") Long id);
}

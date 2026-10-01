package com.limitcross.facility.repository;

import com.limitcross.facility.domain.SearchKeyword;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SearchKeyword entity.
 */
@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {
    default Optional<SearchKeyword> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SearchKeyword> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SearchKeyword> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select searchKeyword from SearchKeyword searchKeyword left join fetch searchKeyword.service",
        countQuery = "select count(searchKeyword) from SearchKeyword searchKeyword"
    )
    Page<SearchKeyword> findAllWithToOneRelationships(Pageable pageable);

    @Query("select searchKeyword from SearchKeyword searchKeyword left join fetch searchKeyword.service")
    List<SearchKeyword> findAllWithToOneRelationships();

    @Query("select searchKeyword from SearchKeyword searchKeyword left join fetch searchKeyword.service where searchKeyword.id =:id")
    Optional<SearchKeyword> findOneWithToOneRelationships(@Param("id") Long id);
}

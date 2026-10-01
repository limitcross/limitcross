package com.limitcross.facility.repository;

import com.limitcross.facility.domain.Professional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProfessionalRepositoryWithBagRelationships {
    Optional<Professional> fetchBagRelationships(Optional<Professional> professional);

    List<Professional> fetchBagRelationships(List<Professional> professionals);

    Page<Professional> fetchBagRelationships(Page<Professional> professionals);
}

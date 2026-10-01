package com.limitcross.facility.repository;

import com.limitcross.facility.domain.Professional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProfessionalRepositoryWithBagRelationshipsImpl implements ProfessionalRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PROFESSIONALS_PARAMETER = "professionals";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Professional> fetchBagRelationships(Optional<Professional> professional) {
        return professional.map(this::fetchZones);
    }

    @Override
    public Page<Professional> fetchBagRelationships(Page<Professional> professionals) {
        return new PageImpl<>(
            fetchBagRelationships(professionals.getContent()),
            professionals.getPageable(),
            professionals.getTotalElements()
        );
    }

    @Override
    public List<Professional> fetchBagRelationships(List<Professional> professionals) {
        return Optional.of(professionals).map(this::fetchZones).orElse(Collections.emptyList());
    }

    Professional fetchZones(Professional result) {
        return entityManager
            .createQuery(
                "select professional from Professional professional left join fetch professional.zones where professional.id = :id",
                Professional.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Professional> fetchZones(List<Professional> professionals) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, professionals.size()).forEach(index -> order.put(professionals.get(index).getId(), index));
        List<Professional> result = entityManager
            .createQuery(
                "select professional from Professional professional left join fetch professional.zones where professional in :professionals",
                Professional.class
            )
            .setParameter(PROFESSIONALS_PARAMETER, professionals)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

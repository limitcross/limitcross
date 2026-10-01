package com.limitcross.facility.service;

import com.limitcross.facility.domain.*; // for static metamodels
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.repository.FacilityServiceRepository;
import com.limitcross.facility.service.criteria.FacilityServiceCriteria;
import com.limitcross.facility.service.dto.FacilityServiceDTO;
import com.limitcross.facility.service.mapper.FacilityServiceMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FacilityService} entities in the database.
 * The main input is a {@link FacilityServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FacilityServiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacilityServiceQueryService extends QueryService<FacilityService> {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityServiceQueryService.class);

    private final FacilityServiceRepository facilityServiceRepository;

    private final FacilityServiceMapper facilityServiceMapper;

    public FacilityServiceQueryService(FacilityServiceRepository facilityServiceRepository, FacilityServiceMapper facilityServiceMapper) {
        this.facilityServiceRepository = facilityServiceRepository;
        this.facilityServiceMapper = facilityServiceMapper;
    }

    /**
     * Return a {@link Page} of {@link FacilityServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacilityServiceDTO> findByCriteria(FacilityServiceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FacilityService> specification = createSpecification(criteria);
        return facilityServiceRepository.findAll(specification, page).map(facilityServiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacilityServiceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FacilityService> specification = createSpecification(criteria);
        return facilityServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link FacilityServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FacilityService> createSpecification(FacilityServiceCriteria criteria) {
        Specification<FacilityService> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FacilityService_.id),
                buildStringSpecification(criteria.getCode(), FacilityService_.code),
                buildStringSpecification(criteria.getTitle(), FacilityService_.title),
                buildStringSpecification(criteria.getSlug(), FacilityService_.slug),
                buildStringSpecification(criteria.getEmoji(), FacilityService_.emoji),
                buildStringSpecification(criteria.getImageUrl(), FacilityService_.imageUrl),
                buildStringSpecification(criteria.getDescription(), FacilityService_.description),
                buildRangeSpecification(criteria.getDurationMinutes(), FacilityService_.durationMinutes),
                buildRangeSpecification(criteria.getWarrantyDays(), FacilityService_.warrantyDays),
                buildStringSpecification(criteria.getSacCode(), FacilityService_.sacCode),
                buildRangeSpecification(criteria.getGstPercent(), FacilityService_.gstPercent),
                buildSpecification(criteria.getGenderSpecific(), FacilityService_.genderSpecific),
                buildSpecification(criteria.getRequiresVisitCharge(), FacilityService_.requiresVisitCharge),
                buildSpecification(criteria.getPopular(), FacilityService_.popular),
                buildSpecification(criteria.getActive(), FacilityService_.active),
                buildRangeSpecification(criteria.getAvgRating(), FacilityService_.avgRating),
                buildRangeSpecification(criteria.getReviewsCount(), FacilityService_.reviewsCount),
                buildRangeSpecification(criteria.getSortOrder(), FacilityService_.sortOrder),
                buildRangeSpecification(criteria.getCreatedAt(), FacilityService_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), FacilityService_.updatedAt),
                buildSpecification(criteria.getServicePackageId(), root ->
                    root.join(FacilityService_.servicePackages, JoinType.LEFT).get(ServicePackage_.id)
                ),
                buildSpecification(criteria.getAddonId(), root -> root.join(FacilityService_.addons, JoinType.LEFT).get(ServiceAddon_.id)),
                buildSpecification(criteria.getTranslationId(), root ->
                    root.join(FacilityService_.translations, JoinType.LEFT).get(ServiceTranslation_.id)
                ),
                buildSpecification(criteria.getCategoryId(), root ->
                    root.join(FacilityService_.category, JoinType.LEFT).get(ServiceCategory_.id)
                )
            );
        }
        return specification;
    }
}

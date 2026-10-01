package com.limitcross.facility.service;

import com.limitcross.facility.domain.*; // for static metamodels
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.repository.ProfessionalRepository;
import com.limitcross.facility.service.criteria.ProfessionalCriteria;
import com.limitcross.facility.service.dto.ProfessionalDTO;
import com.limitcross.facility.service.mapper.ProfessionalMapper;
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
 * Service for executing complex queries for {@link Professional} entities in the database.
 * The main input is a {@link ProfessionalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProfessionalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfessionalQueryService extends QueryService<Professional> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalQueryService.class);

    private final ProfessionalRepository professionalRepository;

    private final ProfessionalMapper professionalMapper;

    public ProfessionalQueryService(ProfessionalRepository professionalRepository, ProfessionalMapper professionalMapper) {
        this.professionalRepository = professionalRepository;
        this.professionalMapper = professionalMapper;
    }

    /**
     * Return a {@link Page} of {@link ProfessionalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalDTO> findByCriteria(ProfessionalCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Professional> specification = createSpecification(criteria);
        return professionalRepository
            .fetchBagRelationships(professionalRepository.findAll(specification, page))
            .map(professionalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfessionalCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Professional> specification = createSpecification(criteria);
        return professionalRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfessionalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Professional> createSpecification(ProfessionalCriteria criteria) {
        Specification<Professional> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Professional_.id),
                buildStringSpecification(criteria.getDisplayName(), Professional_.displayName),
                buildStringSpecification(criteria.getPhotoUrl(), Professional_.photoUrl),
                buildSpecification(criteria.getGender(), Professional_.gender),
                buildSpecification(criteria.getOnboardingStatus(), Professional_.onboardingStatus),
                buildSpecification(criteria.getOnline(), Professional_.online),
                buildRangeSpecification(criteria.getAvgRating(), Professional_.avgRating),
                buildRangeSpecification(criteria.getJobsCompleted(), Professional_.jobsCompleted),
                buildRangeSpecification(criteria.getCancellationRate(), Professional_.cancellationRate),
                buildRangeSpecification(criteria.getLastLat(), Professional_.lastLat),
                buildRangeSpecification(criteria.getLastLng(), Professional_.lastLng),
                buildRangeSpecification(criteria.getLastLocationAt(), Professional_.lastLocationAt),
                buildRangeSpecification(criteria.getJoinedAt(), Professional_.joinedAt),
                buildRangeSpecification(criteria.getCashInHand(), Professional_.cashInHand),
                buildRangeSpecification(criteria.getCashLimit(), Professional_.cashLimit),
                buildRangeSpecification(criteria.getMaxDailyJobs(), Professional_.maxDailyJobs),
                buildStringSpecification(criteria.getLanguages(), Professional_.languages),
                buildStringSpecification(criteria.getBankAccountEnc(), Professional_.bankAccountEnc),
                buildStringSpecification(criteria.getIfscCode(), Professional_.ifscCode),
                buildRangeSpecification(criteria.getDeletedAt(), Professional_.deletedAt),
                buildRangeSpecification(criteria.getCreatedAt(), Professional_.createdAt),
                buildRangeSpecification(criteria.getUpdatedAt(), Professional_.updatedAt),
                buildSpecification(criteria.getUserId(), root -> root.join(Professional_.user, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getKycDocumentId(), root ->
                    root.join(Professional_.kycDocuments, JoinType.LEFT).get(ProfessionalKycDocument_.id)
                ),
                buildSpecification(criteria.getSkillId(), root -> root.join(Professional_.skills, JoinType.LEFT).get(ProfessionalSkill_.id)
                ),
                buildSpecification(criteria.getAvailabilityId(), root ->
                    root.join(Professional_.availabilities, JoinType.LEFT).get(ProfessionalAvailability_.id)
                ),
                buildSpecification(criteria.getTimeOffId(), root ->
                    root.join(Professional_.timeOffs, JoinType.LEFT).get(ProfessionalTimeOff_.id)
                ),
                buildSpecification(criteria.getHomeCityId(), root -> root.join(Professional_.homeCity, JoinType.LEFT).get(City_.id)),
                buildSpecification(criteria.getTierId(), root -> root.join(Professional_.tier, JoinType.LEFT).get(ProfessionalTier_.id)),
                buildSpecification(criteria.getZoneId(), root -> root.join(Professional_.zones, JoinType.LEFT).get(ServiceZone_.id)),
                buildSpecification(criteria.getProfessionalWalletId(), root ->
                    root.join(Professional_.professionalWallet, JoinType.LEFT).get(ProfessionalWallet_.id)
                )
            );
        }
        return specification;
    }
}

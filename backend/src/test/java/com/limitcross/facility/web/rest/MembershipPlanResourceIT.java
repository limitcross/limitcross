package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.MembershipPlanAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.MembershipPlan;
import com.limitcross.facility.repository.MembershipPlanRepository;
import com.limitcross.facility.service.dto.MembershipPlanDTO;
import com.limitcross.facility.service.mapper.MembershipPlanMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MembershipPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MembershipPlanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final Integer DEFAULT_DURATION_DAYS = 1;
    private static final Integer UPDATED_DURATION_DAYS = 2;

    private static final BigDecimal DEFAULT_DISCOUNT_PERCENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT_PERCENT = new BigDecimal(1);

    private static final Boolean DEFAULT_FREE_VISIT_CHARGE = false;
    private static final Boolean UPDATED_FREE_VISIT_CHARGE = true;

    private static final Boolean DEFAULT_PRIORITY_SUPPORT = false;
    private static final Boolean UPDATED_PRIORITY_SUPPORT = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/membership-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private MembershipPlanMapper membershipPlanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipPlanMockMvc;

    private MembershipPlan membershipPlan;

    private MembershipPlan insertedMembershipPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipPlan createEntity() {
        return new MembershipPlan()
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .durationDays(DEFAULT_DURATION_DAYS)
            .discountPercent(DEFAULT_DISCOUNT_PERCENT)
            .freeVisitCharge(DEFAULT_FREE_VISIT_CHARGE)
            .prioritySupport(DEFAULT_PRIORITY_SUPPORT)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipPlan createUpdatedEntity() {
        return new MembershipPlan()
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .durationDays(UPDATED_DURATION_DAYS)
            .discountPercent(UPDATED_DISCOUNT_PERCENT)
            .freeVisitCharge(UPDATED_FREE_VISIT_CHARGE)
            .prioritySupport(UPDATED_PRIORITY_SUPPORT)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        membershipPlan = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMembershipPlan != null) {
            membershipPlanRepository.delete(insertedMembershipPlan);
            insertedMembershipPlan = null;
        }
    }

    @Test
    @Transactional
    void createMembershipPlan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MembershipPlan
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);
        var returnedMembershipPlanDTO = om.readValue(
            restMembershipPlanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipPlanDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MembershipPlanDTO.class
        );

        // Validate the MembershipPlan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMembershipPlan = membershipPlanMapper.toEntity(returnedMembershipPlanDTO);
        assertMembershipPlanUpdatableFieldsEquals(returnedMembershipPlan, getPersistedMembershipPlan(returnedMembershipPlan));

        insertedMembershipPlan = returnedMembershipPlan;
    }

    @Test
    @Transactional
    void createMembershipPlanWithExistingId() throws Exception {
        // Create the MembershipPlan with an existing ID
        membershipPlan.setId(1L);
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        membershipPlan.setName(null);

        // Create the MembershipPlan, which fails.
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        restMembershipPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        membershipPlan.setPrice(null);

        // Create the MembershipPlan, which fails.
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        restMembershipPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationDaysIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        membershipPlan.setDurationDays(null);

        // Create the MembershipPlan, which fails.
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        restMembershipPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        membershipPlan.setActive(null);

        // Create the MembershipPlan, which fails.
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        restMembershipPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembershipPlans() throws Exception {
        // Initialize the database
        insertedMembershipPlan = membershipPlanRepository.saveAndFlush(membershipPlan);

        // Get all the membershipPlanList
        restMembershipPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].durationDays").value(hasItem(DEFAULT_DURATION_DAYS)))
            .andExpect(jsonPath("$.[*].discountPercent").value(hasItem(sameNumber(DEFAULT_DISCOUNT_PERCENT))))
            .andExpect(jsonPath("$.[*].freeVisitCharge").value(hasItem(DEFAULT_FREE_VISIT_CHARGE)))
            .andExpect(jsonPath("$.[*].prioritySupport").value(hasItem(DEFAULT_PRIORITY_SUPPORT)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getMembershipPlan() throws Exception {
        // Initialize the database
        insertedMembershipPlan = membershipPlanRepository.saveAndFlush(membershipPlan);

        // Get the membershipPlan
        restMembershipPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, membershipPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipPlan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.durationDays").value(DEFAULT_DURATION_DAYS))
            .andExpect(jsonPath("$.discountPercent").value(sameNumber(DEFAULT_DISCOUNT_PERCENT)))
            .andExpect(jsonPath("$.freeVisitCharge").value(DEFAULT_FREE_VISIT_CHARGE))
            .andExpect(jsonPath("$.prioritySupport").value(DEFAULT_PRIORITY_SUPPORT))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingMembershipPlan() throws Exception {
        // Get the membershipPlan
        restMembershipPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMembershipPlan() throws Exception {
        // Initialize the database
        insertedMembershipPlan = membershipPlanRepository.saveAndFlush(membershipPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the membershipPlan
        MembershipPlan updatedMembershipPlan = membershipPlanRepository.findById(membershipPlan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMembershipPlan are not directly saved in db
        em.detach(updatedMembershipPlan);
        updatedMembershipPlan
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .durationDays(UPDATED_DURATION_DAYS)
            .discountPercent(UPDATED_DISCOUNT_PERCENT)
            .freeVisitCharge(UPDATED_FREE_VISIT_CHARGE)
            .prioritySupport(UPDATED_PRIORITY_SUPPORT)
            .active(UPDATED_ACTIVE);
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(updatedMembershipPlan);

        restMembershipPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membershipPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(membershipPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the MembershipPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMembershipPlanToMatchAllProperties(updatedMembershipPlan);
    }

    @Test
    @Transactional
    void putNonExistingMembershipPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipPlan.setId(longCount.incrementAndGet());

        // Create the MembershipPlan
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membershipPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(membershipPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMembershipPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipPlan.setId(longCount.incrementAndGet());

        // Create the MembershipPlan
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(membershipPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMembershipPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipPlan.setId(longCount.incrementAndGet());

        // Create the MembershipPlan
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MembershipPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMembershipPlanWithPatch() throws Exception {
        // Initialize the database
        insertedMembershipPlan = membershipPlanRepository.saveAndFlush(membershipPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the membershipPlan using partial update
        MembershipPlan partialUpdatedMembershipPlan = new MembershipPlan();
        partialUpdatedMembershipPlan.setId(membershipPlan.getId());

        partialUpdatedMembershipPlan.price(UPDATED_PRICE).active(UPDATED_ACTIVE);

        restMembershipPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembershipPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMembershipPlan))
            )
            .andExpect(status().isOk());

        // Validate the MembershipPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMembershipPlanUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMembershipPlan, membershipPlan),
            getPersistedMembershipPlan(membershipPlan)
        );
    }

    @Test
    @Transactional
    void fullUpdateMembershipPlanWithPatch() throws Exception {
        // Initialize the database
        insertedMembershipPlan = membershipPlanRepository.saveAndFlush(membershipPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the membershipPlan using partial update
        MembershipPlan partialUpdatedMembershipPlan = new MembershipPlan();
        partialUpdatedMembershipPlan.setId(membershipPlan.getId());

        partialUpdatedMembershipPlan
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .durationDays(UPDATED_DURATION_DAYS)
            .discountPercent(UPDATED_DISCOUNT_PERCENT)
            .freeVisitCharge(UPDATED_FREE_VISIT_CHARGE)
            .prioritySupport(UPDATED_PRIORITY_SUPPORT)
            .active(UPDATED_ACTIVE);

        restMembershipPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembershipPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMembershipPlan))
            )
            .andExpect(status().isOk());

        // Validate the MembershipPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMembershipPlanUpdatableFieldsEquals(partialUpdatedMembershipPlan, getPersistedMembershipPlan(partialUpdatedMembershipPlan));
    }

    @Test
    @Transactional
    void patchNonExistingMembershipPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipPlan.setId(longCount.incrementAndGet());

        // Create the MembershipPlan
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, membershipPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(membershipPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMembershipPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipPlan.setId(longCount.incrementAndGet());

        // Create the MembershipPlan
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(membershipPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMembershipPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipPlan.setId(longCount.incrementAndGet());

        // Create the MembershipPlan
        MembershipPlanDTO membershipPlanDTO = membershipPlanMapper.toDto(membershipPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(membershipPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MembershipPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMembershipPlan() throws Exception {
        // Initialize the database
        insertedMembershipPlan = membershipPlanRepository.saveAndFlush(membershipPlan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the membershipPlan
        restMembershipPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, membershipPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return membershipPlanRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MembershipPlan getPersistedMembershipPlan(MembershipPlan membershipPlan) {
        return membershipPlanRepository.findById(membershipPlan.getId()).orElseThrow();
    }

    protected void assertPersistedMembershipPlanToMatchAllProperties(MembershipPlan expectedMembershipPlan) {
        assertMembershipPlanAllPropertiesEquals(expectedMembershipPlan, getPersistedMembershipPlan(expectedMembershipPlan));
    }

    protected void assertPersistedMembershipPlanToMatchUpdatableProperties(MembershipPlan expectedMembershipPlan) {
        assertMembershipPlanAllUpdatablePropertiesEquals(expectedMembershipPlan, getPersistedMembershipPlan(expectedMembershipPlan));
    }
}

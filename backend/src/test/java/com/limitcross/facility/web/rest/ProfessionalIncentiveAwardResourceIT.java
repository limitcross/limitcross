package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalIncentiveAwardAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.IncentiveRule;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.ProfessionalIncentiveAward;
import com.limitcross.facility.repository.ProfessionalIncentiveAwardRepository;
import com.limitcross.facility.service.ProfessionalIncentiveAwardService;
import com.limitcross.facility.service.dto.ProfessionalIncentiveAwardDTO;
import com.limitcross.facility.service.mapper.ProfessionalIncentiveAwardMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProfessionalIncentiveAwardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalIncentiveAwardResourceIT {

    private static final LocalDate DEFAULT_PERIOD_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIOD_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_END = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/professional-incentive-awards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalIncentiveAwardRepository professionalIncentiveAwardRepository;

    @Mock
    private ProfessionalIncentiveAwardRepository professionalIncentiveAwardRepositoryMock;

    @Autowired
    private ProfessionalIncentiveAwardMapper professionalIncentiveAwardMapper;

    @Mock
    private ProfessionalIncentiveAwardService professionalIncentiveAwardServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalIncentiveAwardMockMvc;

    private ProfessionalIncentiveAward professionalIncentiveAward;

    private ProfessionalIncentiveAward insertedProfessionalIncentiveAward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalIncentiveAward createEntity(EntityManager em) {
        ProfessionalIncentiveAward professionalIncentiveAward = new ProfessionalIncentiveAward()
            .periodStart(DEFAULT_PERIOD_START)
            .periodEnd(DEFAULT_PERIOD_END)
            .amount(DEFAULT_AMOUNT)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        professionalIncentiveAward.setProfessional(professional);
        // Add required entity
        IncentiveRule incentiveRule;
        if (TestUtil.findAll(em, IncentiveRule.class).isEmpty()) {
            incentiveRule = IncentiveRuleResourceIT.createEntity();
            em.persist(incentiveRule);
            em.flush();
        } else {
            incentiveRule = TestUtil.findAll(em, IncentiveRule.class).get(0);
        }
        professionalIncentiveAward.setRule(incentiveRule);
        return professionalIncentiveAward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalIncentiveAward createUpdatedEntity(EntityManager em) {
        ProfessionalIncentiveAward updatedProfessionalIncentiveAward = new ProfessionalIncentiveAward()
            .periodStart(UPDATED_PERIOD_START)
            .periodEnd(UPDATED_PERIOD_END)
            .amount(UPDATED_AMOUNT)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedProfessionalIncentiveAward.setProfessional(professional);
        // Add required entity
        IncentiveRule incentiveRule;
        if (TestUtil.findAll(em, IncentiveRule.class).isEmpty()) {
            incentiveRule = IncentiveRuleResourceIT.createUpdatedEntity();
            em.persist(incentiveRule);
            em.flush();
        } else {
            incentiveRule = TestUtil.findAll(em, IncentiveRule.class).get(0);
        }
        updatedProfessionalIncentiveAward.setRule(incentiveRule);
        return updatedProfessionalIncentiveAward;
    }

    @BeforeEach
    void initTest() {
        professionalIncentiveAward = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalIncentiveAward != null) {
            professionalIncentiveAwardRepository.delete(insertedProfessionalIncentiveAward);
            insertedProfessionalIncentiveAward = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalIncentiveAward() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalIncentiveAward
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);
        var returnedProfessionalIncentiveAwardDTO = om.readValue(
            restProfessionalIncentiveAwardMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalIncentiveAwardDTO.class
        );

        // Validate the ProfessionalIncentiveAward in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalIncentiveAward = professionalIncentiveAwardMapper.toEntity(returnedProfessionalIncentiveAwardDTO);
        assertProfessionalIncentiveAwardUpdatableFieldsEquals(
            returnedProfessionalIncentiveAward,
            getPersistedProfessionalIncentiveAward(returnedProfessionalIncentiveAward)
        );

        insertedProfessionalIncentiveAward = returnedProfessionalIncentiveAward;
    }

    @Test
    @Transactional
    void createProfessionalIncentiveAwardWithExistingId() throws Exception {
        // Create the ProfessionalIncentiveAward with an existing ID
        professionalIncentiveAward.setId(1L);
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalIncentiveAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalIncentiveAward in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPeriodStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalIncentiveAward.setPeriodStart(null);

        // Create the ProfessionalIncentiveAward, which fails.
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        restProfessionalIncentiveAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalIncentiveAward.setPeriodEnd(null);

        // Create the ProfessionalIncentiveAward, which fails.
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        restProfessionalIncentiveAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalIncentiveAward.setAmount(null);

        // Create the ProfessionalIncentiveAward, which fails.
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        restProfessionalIncentiveAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalIncentiveAwards() throws Exception {
        // Initialize the database
        insertedProfessionalIncentiveAward = professionalIncentiveAwardRepository.saveAndFlush(professionalIncentiveAward);

        // Get all the professionalIncentiveAwardList
        restProfessionalIncentiveAwardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalIncentiveAward.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodStart").value(hasItem(DEFAULT_PERIOD_START.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalIncentiveAwardsWithEagerRelationshipsIsEnabled() throws Exception {
        when(professionalIncentiveAwardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalIncentiveAwardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professionalIncentiveAwardServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfessionalIncentiveAwardsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professionalIncentiveAwardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfessionalIncentiveAwardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professionalIncentiveAwardRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfessionalIncentiveAward() throws Exception {
        // Initialize the database
        insertedProfessionalIncentiveAward = professionalIncentiveAwardRepository.saveAndFlush(professionalIncentiveAward);

        // Get the professionalIncentiveAward
        restProfessionalIncentiveAwardMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalIncentiveAward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalIncentiveAward.getId().intValue()))
            .andExpect(jsonPath("$.periodStart").value(DEFAULT_PERIOD_START.toString()))
            .andExpect(jsonPath("$.periodEnd").value(DEFAULT_PERIOD_END.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalIncentiveAward() throws Exception {
        // Get the professionalIncentiveAward
        restProfessionalIncentiveAwardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalIncentiveAward() throws Exception {
        // Initialize the database
        insertedProfessionalIncentiveAward = professionalIncentiveAwardRepository.saveAndFlush(professionalIncentiveAward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalIncentiveAward
        ProfessionalIncentiveAward updatedProfessionalIncentiveAward = professionalIncentiveAwardRepository
            .findById(professionalIncentiveAward.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalIncentiveAward are not directly saved in db
        em.detach(updatedProfessionalIncentiveAward);
        updatedProfessionalIncentiveAward
            .periodStart(UPDATED_PERIOD_START)
            .periodEnd(UPDATED_PERIOD_END)
            .amount(UPDATED_AMOUNT)
            .createdAt(UPDATED_CREATED_AT);
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(
            updatedProfessionalIncentiveAward
        );

        restProfessionalIncentiveAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalIncentiveAwardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalIncentiveAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalIncentiveAwardToMatchAllProperties(updatedProfessionalIncentiveAward);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalIncentiveAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalIncentiveAward.setId(longCount.incrementAndGet());

        // Create the ProfessionalIncentiveAward
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalIncentiveAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalIncentiveAwardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalIncentiveAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalIncentiveAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalIncentiveAward.setId(longCount.incrementAndGet());

        // Create the ProfessionalIncentiveAward
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalIncentiveAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalIncentiveAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalIncentiveAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalIncentiveAward.setId(longCount.incrementAndGet());

        // Create the ProfessionalIncentiveAward
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalIncentiveAwardMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalIncentiveAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalIncentiveAwardWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalIncentiveAward = professionalIncentiveAwardRepository.saveAndFlush(professionalIncentiveAward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalIncentiveAward using partial update
        ProfessionalIncentiveAward partialUpdatedProfessionalIncentiveAward = new ProfessionalIncentiveAward();
        partialUpdatedProfessionalIncentiveAward.setId(professionalIncentiveAward.getId());

        partialUpdatedProfessionalIncentiveAward.createdAt(UPDATED_CREATED_AT);

        restProfessionalIncentiveAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalIncentiveAward.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalIncentiveAward))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalIncentiveAward in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalIncentiveAwardUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalIncentiveAward, professionalIncentiveAward),
            getPersistedProfessionalIncentiveAward(professionalIncentiveAward)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalIncentiveAwardWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalIncentiveAward = professionalIncentiveAwardRepository.saveAndFlush(professionalIncentiveAward);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalIncentiveAward using partial update
        ProfessionalIncentiveAward partialUpdatedProfessionalIncentiveAward = new ProfessionalIncentiveAward();
        partialUpdatedProfessionalIncentiveAward.setId(professionalIncentiveAward.getId());

        partialUpdatedProfessionalIncentiveAward
            .periodStart(UPDATED_PERIOD_START)
            .periodEnd(UPDATED_PERIOD_END)
            .amount(UPDATED_AMOUNT)
            .createdAt(UPDATED_CREATED_AT);

        restProfessionalIncentiveAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalIncentiveAward.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalIncentiveAward))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalIncentiveAward in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalIncentiveAwardUpdatableFieldsEquals(
            partialUpdatedProfessionalIncentiveAward,
            getPersistedProfessionalIncentiveAward(partialUpdatedProfessionalIncentiveAward)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalIncentiveAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalIncentiveAward.setId(longCount.incrementAndGet());

        // Create the ProfessionalIncentiveAward
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalIncentiveAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalIncentiveAwardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalIncentiveAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalIncentiveAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalIncentiveAward.setId(longCount.incrementAndGet());

        // Create the ProfessionalIncentiveAward
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalIncentiveAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalIncentiveAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalIncentiveAward() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalIncentiveAward.setId(longCount.incrementAndGet());

        // Create the ProfessionalIncentiveAward
        ProfessionalIncentiveAwardDTO professionalIncentiveAwardDTO = professionalIncentiveAwardMapper.toDto(professionalIncentiveAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalIncentiveAwardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalIncentiveAwardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalIncentiveAward in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalIncentiveAward() throws Exception {
        // Initialize the database
        insertedProfessionalIncentiveAward = professionalIncentiveAwardRepository.saveAndFlush(professionalIncentiveAward);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalIncentiveAward
        restProfessionalIncentiveAwardMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalIncentiveAward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalIncentiveAwardRepository.count();
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

    protected ProfessionalIncentiveAward getPersistedProfessionalIncentiveAward(ProfessionalIncentiveAward professionalIncentiveAward) {
        return professionalIncentiveAwardRepository.findById(professionalIncentiveAward.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalIncentiveAwardToMatchAllProperties(
        ProfessionalIncentiveAward expectedProfessionalIncentiveAward
    ) {
        assertProfessionalIncentiveAwardAllPropertiesEquals(
            expectedProfessionalIncentiveAward,
            getPersistedProfessionalIncentiveAward(expectedProfessionalIncentiveAward)
        );
    }

    protected void assertPersistedProfessionalIncentiveAwardToMatchUpdatableProperties(
        ProfessionalIncentiveAward expectedProfessionalIncentiveAward
    ) {
        assertProfessionalIncentiveAwardAllUpdatablePropertiesEquals(
            expectedProfessionalIncentiveAward,
            getPersistedProfessionalIncentiveAward(expectedProfessionalIncentiveAward)
        );
    }
}

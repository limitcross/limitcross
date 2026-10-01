package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.PayoutAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Payout;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.enumeration.PayoutStatus;
import com.limitcross.facility.repository.PayoutRepository;
import com.limitcross.facility.service.PayoutService;
import com.limitcross.facility.service.dto.PayoutDTO;
import com.limitcross.facility.service.mapper.PayoutMapper;
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
 * Integration tests for the {@link PayoutResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PayoutResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final PayoutStatus DEFAULT_STATUS = PayoutStatus.PENDING;
    private static final PayoutStatus UPDATED_STATUS = PayoutStatus.PAID;

    private static final String DEFAULT_BANK_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_BANK_REFERENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PERIOD_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIOD_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_END = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PAID_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAID_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/payouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PayoutRepository payoutRepository;

    @Mock
    private PayoutRepository payoutRepositoryMock;

    @Autowired
    private PayoutMapper payoutMapper;

    @Mock
    private PayoutService payoutServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayoutMockMvc;

    private Payout payout;

    private Payout insertedPayout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payout createEntity(EntityManager em) {
        Payout payout = new Payout()
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS)
            .bankReference(DEFAULT_BANK_REFERENCE)
            .periodStart(DEFAULT_PERIOD_START)
            .periodEnd(DEFAULT_PERIOD_END)
            .createdAt(DEFAULT_CREATED_AT)
            .paidAt(DEFAULT_PAID_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        payout.setProfessional(professional);
        return payout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payout createUpdatedEntity(EntityManager em) {
        Payout updatedPayout = new Payout()
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .bankReference(UPDATED_BANK_REFERENCE)
            .periodStart(UPDATED_PERIOD_START)
            .periodEnd(UPDATED_PERIOD_END)
            .createdAt(UPDATED_CREATED_AT)
            .paidAt(UPDATED_PAID_AT);
        // Add required entity
        Professional professional;
        if (TestUtil.findAll(em, Professional.class).isEmpty()) {
            professional = ProfessionalResourceIT.createUpdatedEntity(em);
            em.persist(professional);
            em.flush();
        } else {
            professional = TestUtil.findAll(em, Professional.class).get(0);
        }
        updatedPayout.setProfessional(professional);
        return updatedPayout;
    }

    @BeforeEach
    void initTest() {
        payout = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPayout != null) {
            payoutRepository.delete(insertedPayout);
            insertedPayout = null;
        }
    }

    @Test
    @Transactional
    void createPayout() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payout
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);
        var returnedPayoutDTO = om.readValue(
            restPayoutMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PayoutDTO.class
        );

        // Validate the Payout in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPayout = payoutMapper.toEntity(returnedPayoutDTO);
        assertPayoutUpdatableFieldsEquals(returnedPayout, getPersistedPayout(returnedPayout));

        insertedPayout = returnedPayout;
    }

    @Test
    @Transactional
    void createPayoutWithExistingId() throws Exception {
        // Create the Payout with an existing ID
        payout.setId(1L);
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payout in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payout.setAmount(null);

        // Create the Payout, which fails.
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        restPayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payout.setStatus(null);

        // Create the Payout, which fails.
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        restPayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payout.setPeriodStart(null);

        // Create the Payout, which fails.
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        restPayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        payout.setPeriodEnd(null);

        // Create the Payout, which fails.
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        restPayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayouts() throws Exception {
        // Initialize the database
        insertedPayout = payoutRepository.saveAndFlush(payout);

        // Get all the payoutList
        restPayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payout.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bankReference").value(hasItem(DEFAULT_BANK_REFERENCE)))
            .andExpect(jsonPath("$.[*].periodStart").value(hasItem(DEFAULT_PERIOD_START.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].paidAt").value(hasItem(DEFAULT_PAID_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPayoutsWithEagerRelationshipsIsEnabled() throws Exception {
        when(payoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPayoutMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(payoutServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPayoutsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(payoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPayoutMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(payoutRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPayout() throws Exception {
        // Initialize the database
        insertedPayout = payoutRepository.saveAndFlush(payout);

        // Get the payout
        restPayoutMockMvc
            .perform(get(ENTITY_API_URL_ID, payout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payout.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.bankReference").value(DEFAULT_BANK_REFERENCE))
            .andExpect(jsonPath("$.periodStart").value(DEFAULT_PERIOD_START.toString()))
            .andExpect(jsonPath("$.periodEnd").value(DEFAULT_PERIOD_END.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.paidAt").value(DEFAULT_PAID_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPayout() throws Exception {
        // Get the payout
        restPayoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayout() throws Exception {
        // Initialize the database
        insertedPayout = payoutRepository.saveAndFlush(payout);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payout
        Payout updatedPayout = payoutRepository.findById(payout.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayout are not directly saved in db
        em.detach(updatedPayout);
        updatedPayout
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .bankReference(UPDATED_BANK_REFERENCE)
            .periodStart(UPDATED_PERIOD_START)
            .periodEnd(UPDATED_PERIOD_END)
            .createdAt(UPDATED_CREATED_AT)
            .paidAt(UPDATED_PAID_AT);
        PayoutDTO payoutDTO = payoutMapper.toDto(updatedPayout);

        restPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payoutDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO))
            )
            .andExpect(status().isOk());

        // Validate the Payout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPayoutToMatchAllProperties(updatedPayout);
    }

    @Test
    @Transactional
    void putNonExistingPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payout.setId(longCount.incrementAndGet());

        // Create the Payout
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payoutDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payout.setId(longCount.incrementAndGet());

        // Create the Payout
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(payoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payout.setId(longCount.incrementAndGet());

        // Create the Payout
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(payoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePayoutWithPatch() throws Exception {
        // Initialize the database
        insertedPayout = payoutRepository.saveAndFlush(payout);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payout using partial update
        Payout partialUpdatedPayout = new Payout();
        partialUpdatedPayout.setId(payout.getId());

        partialUpdatedPayout
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .bankReference(UPDATED_BANK_REFERENCE)
            .createdAt(UPDATED_CREATED_AT)
            .paidAt(UPDATED_PAID_AT);

        restPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayout))
            )
            .andExpect(status().isOk());

        // Validate the Payout in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPayoutUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayout, payout), getPersistedPayout(payout));
    }

    @Test
    @Transactional
    void fullUpdatePayoutWithPatch() throws Exception {
        // Initialize the database
        insertedPayout = payoutRepository.saveAndFlush(payout);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payout using partial update
        Payout partialUpdatedPayout = new Payout();
        partialUpdatedPayout.setId(payout.getId());

        partialUpdatedPayout
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .bankReference(UPDATED_BANK_REFERENCE)
            .periodStart(UPDATED_PERIOD_START)
            .periodEnd(UPDATED_PERIOD_END)
            .createdAt(UPDATED_CREATED_AT)
            .paidAt(UPDATED_PAID_AT);

        restPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayout))
            )
            .andExpect(status().isOk());

        // Validate the Payout in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPayoutUpdatableFieldsEquals(partialUpdatedPayout, getPersistedPayout(partialUpdatedPayout));
    }

    @Test
    @Transactional
    void patchNonExistingPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payout.setId(longCount.incrementAndGet());

        // Create the Payout
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payoutDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(payoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payout.setId(longCount.incrementAndGet());

        // Create the Payout
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(payoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payout.setId(longCount.incrementAndGet());

        // Create the Payout
        PayoutDTO payoutDTO = payoutMapper.toDto(payout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayoutMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(payoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayout() throws Exception {
        // Initialize the database
        insertedPayout = payoutRepository.saveAndFlush(payout);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payout
        restPayoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, payout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return payoutRepository.count();
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

    protected Payout getPersistedPayout(Payout payout) {
        return payoutRepository.findById(payout.getId()).orElseThrow();
    }

    protected void assertPersistedPayoutToMatchAllProperties(Payout expectedPayout) {
        assertPayoutAllPropertiesEquals(expectedPayout, getPersistedPayout(expectedPayout));
    }

    protected void assertPersistedPayoutToMatchUpdatableProperties(Payout expectedPayout) {
        assertPayoutAllUpdatablePropertiesEquals(expectedPayout, getPersistedPayout(expectedPayout));
    }
}

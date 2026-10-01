package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.WalletTransactionAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Professional;
import com.limitcross.facility.domain.WalletTransaction;
import com.limitcross.facility.domain.enumeration.WalletTxnType;
import com.limitcross.facility.repository.WalletTransactionRepository;
import com.limitcross.facility.service.WalletTransactionService;
import com.limitcross.facility.service.dto.WalletTransactionDTO;
import com.limitcross.facility.service.mapper.WalletTransactionMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link WalletTransactionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WalletTransactionResourceIT {

    private static final WalletTxnType DEFAULT_TXN_TYPE = WalletTxnType.EARNING;
    private static final WalletTxnType UPDATED_TXN_TYPE = WalletTxnType.COMMISSION;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BALANCE_AFTER = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE_AFTER = new BigDecimal(2);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/wallet-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepositoryMock;

    @Autowired
    private WalletTransactionMapper walletTransactionMapper;

    @Mock
    private WalletTransactionService walletTransactionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalletTransactionMockMvc;

    private WalletTransaction walletTransaction;

    private WalletTransaction insertedWalletTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletTransaction createEntity(EntityManager em) {
        WalletTransaction walletTransaction = new WalletTransaction()
            .txnType(DEFAULT_TXN_TYPE)
            .amount(DEFAULT_AMOUNT)
            .balanceAfter(DEFAULT_BALANCE_AFTER)
            .description(DEFAULT_DESCRIPTION)
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
        walletTransaction.setProfessional(professional);
        return walletTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletTransaction createUpdatedEntity(EntityManager em) {
        WalletTransaction updatedWalletTransaction = new WalletTransaction()
            .txnType(UPDATED_TXN_TYPE)
            .amount(UPDATED_AMOUNT)
            .balanceAfter(UPDATED_BALANCE_AFTER)
            .description(UPDATED_DESCRIPTION)
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
        updatedWalletTransaction.setProfessional(professional);
        return updatedWalletTransaction;
    }

    @BeforeEach
    void initTest() {
        walletTransaction = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedWalletTransaction != null) {
            walletTransactionRepository.delete(insertedWalletTransaction);
            insertedWalletTransaction = null;
        }
    }

    @Test
    @Transactional
    void createWalletTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);
        var returnedWalletTransactionDTO = om.readValue(
            restWalletTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WalletTransactionDTO.class
        );

        // Validate the WalletTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWalletTransaction = walletTransactionMapper.toEntity(returnedWalletTransactionDTO);
        assertWalletTransactionUpdatableFieldsEquals(returnedWalletTransaction, getPersistedWalletTransaction(returnedWalletTransaction));

        insertedWalletTransaction = returnedWalletTransaction;
    }

    @Test
    @Transactional
    void createWalletTransactionWithExistingId() throws Exception {
        // Create the WalletTransaction with an existing ID
        walletTransaction.setId(1L);
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTxnTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        walletTransaction.setTxnType(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        walletTransaction.setAmount(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBalanceAfterIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        walletTransaction.setBalanceAfter(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        walletTransaction.setCreatedAt(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        restWalletTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWalletTransactions() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        // Get all the walletTransactionList
        restWalletTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].txnType").value(hasItem(DEFAULT_TXN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].balanceAfter").value(hasItem(sameNumber(DEFAULT_BALANCE_AFTER))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletTransactionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(walletTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWalletTransactionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(walletTransactionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletTransactionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(walletTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWalletTransactionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(walletTransactionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWalletTransaction() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        // Get the walletTransaction
        restWalletTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, walletTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(walletTransaction.getId().intValue()))
            .andExpect(jsonPath("$.txnType").value(DEFAULT_TXN_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.balanceAfter").value(sameNumber(DEFAULT_BALANCE_AFTER)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWalletTransaction() throws Exception {
        // Get the walletTransaction
        restWalletTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWalletTransaction() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletTransaction
        WalletTransaction updatedWalletTransaction = walletTransactionRepository.findById(walletTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWalletTransaction are not directly saved in db
        em.detach(updatedWalletTransaction);
        updatedWalletTransaction
            .txnType(UPDATED_TXN_TYPE)
            .amount(UPDATED_AMOUNT)
            .balanceAfter(UPDATED_BALANCE_AFTER)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(updatedWalletTransaction);

        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, walletTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWalletTransactionToMatchAllProperties(updatedWalletTransaction);
    }

    @Test
    @Transactional
    void putNonExistingWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, walletTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalletTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletTransaction using partial update
        WalletTransaction partialUpdatedWalletTransaction = new WalletTransaction();
        partialUpdatedWalletTransaction.setId(walletTransaction.getId());

        partialUpdatedWalletTransaction.txnType(UPDATED_TXN_TYPE).description(UPDATED_DESCRIPTION);

        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWalletTransaction))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWalletTransaction, walletTransaction),
            getPersistedWalletTransaction(walletTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateWalletTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletTransaction using partial update
        WalletTransaction partialUpdatedWalletTransaction = new WalletTransaction();
        partialUpdatedWalletTransaction.setId(walletTransaction.getId());

        partialUpdatedWalletTransaction
            .txnType(UPDATED_TXN_TYPE)
            .amount(UPDATED_AMOUNT)
            .balanceAfter(UPDATED_BALANCE_AFTER)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);

        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWalletTransaction))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletTransactionUpdatableFieldsEquals(
            partialUpdatedWalletTransaction,
            getPersistedWalletTransaction(partialUpdatedWalletTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, walletTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(walletTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(walletTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWalletTransaction() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the walletTransaction
        restWalletTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, walletTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return walletTransactionRepository.count();
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

    protected WalletTransaction getPersistedWalletTransaction(WalletTransaction walletTransaction) {
        return walletTransactionRepository.findById(walletTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedWalletTransactionToMatchAllProperties(WalletTransaction expectedWalletTransaction) {
        assertWalletTransactionAllPropertiesEquals(expectedWalletTransaction, getPersistedWalletTransaction(expectedWalletTransaction));
    }

    protected void assertPersistedWalletTransactionToMatchUpdatableProperties(WalletTransaction expectedWalletTransaction) {
        assertWalletTransactionAllUpdatablePropertiesEquals(
            expectedWalletTransaction,
            getPersistedWalletTransaction(expectedWalletTransaction)
        );
    }
}

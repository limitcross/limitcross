package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CustomerWalletTxnAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.CustomerWalletTxn;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.enumeration.CustomerWalletTxnType;
import com.limitcross.facility.repository.CustomerWalletTxnRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.CustomerWalletTxnService;
import com.limitcross.facility.service.dto.CustomerWalletTxnDTO;
import com.limitcross.facility.service.mapper.CustomerWalletTxnMapper;
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
 * Integration tests for the {@link CustomerWalletTxnResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerWalletTxnResourceIT {

    private static final CustomerWalletTxnType DEFAULT_TXN_TYPE = CustomerWalletTxnType.TOPUP;
    private static final CustomerWalletTxnType UPDATED_TXN_TYPE = CustomerWalletTxnType.SPEND;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BALANCE_AFTER = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE_AFTER = new BigDecimal(2);

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/customer-wallet-txns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerWalletTxnRepository customerWalletTxnRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CustomerWalletTxnRepository customerWalletTxnRepositoryMock;

    @Autowired
    private CustomerWalletTxnMapper customerWalletTxnMapper;

    @Mock
    private CustomerWalletTxnService customerWalletTxnServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerWalletTxnMockMvc;

    private CustomerWalletTxn customerWalletTxn;

    private CustomerWalletTxn insertedCustomerWalletTxn;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerWalletTxn createEntity(EntityManager em) {
        CustomerWalletTxn customerWalletTxn = new CustomerWalletTxn()
            .txnType(DEFAULT_TXN_TYPE)
            .amount(DEFAULT_AMOUNT)
            .balanceAfter(DEFAULT_BALANCE_AFTER)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .note(DEFAULT_NOTE)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        customerWalletTxn.setUser(user);
        return customerWalletTxn;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerWalletTxn createUpdatedEntity(EntityManager em) {
        CustomerWalletTxn updatedCustomerWalletTxn = new CustomerWalletTxn()
            .txnType(UPDATED_TXN_TYPE)
            .amount(UPDATED_AMOUNT)
            .balanceAfter(UPDATED_BALANCE_AFTER)
            .expiresAt(UPDATED_EXPIRES_AT)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCustomerWalletTxn.setUser(user);
        return updatedCustomerWalletTxn;
    }

    @BeforeEach
    void initTest() {
        customerWalletTxn = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomerWalletTxn != null) {
            customerWalletTxnRepository.delete(insertedCustomerWalletTxn);
            insertedCustomerWalletTxn = null;
        }
    }

    @Test
    @Transactional
    void createCustomerWalletTxn() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerWalletTxn
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);
        var returnedCustomerWalletTxnDTO = om.readValue(
            restCustomerWalletTxnMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletTxnDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerWalletTxnDTO.class
        );

        // Validate the CustomerWalletTxn in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerWalletTxn = customerWalletTxnMapper.toEntity(returnedCustomerWalletTxnDTO);
        assertCustomerWalletTxnUpdatableFieldsEquals(returnedCustomerWalletTxn, getPersistedCustomerWalletTxn(returnedCustomerWalletTxn));

        insertedCustomerWalletTxn = returnedCustomerWalletTxn;
    }

    @Test
    @Transactional
    void createCustomerWalletTxnWithExistingId() throws Exception {
        // Create the CustomerWalletTxn with an existing ID
        customerWalletTxn.setId(1L);
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerWalletTxnMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletTxnDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerWalletTxn in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTxnTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerWalletTxn.setTxnType(null);

        // Create the CustomerWalletTxn, which fails.
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        restCustomerWalletTxnMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletTxnDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerWalletTxn.setAmount(null);

        // Create the CustomerWalletTxn, which fails.
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        restCustomerWalletTxnMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletTxnDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBalanceAfterIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerWalletTxn.setBalanceAfter(null);

        // Create the CustomerWalletTxn, which fails.
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        restCustomerWalletTxnMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletTxnDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerWalletTxn.setCreatedAt(null);

        // Create the CustomerWalletTxn, which fails.
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        restCustomerWalletTxnMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletTxnDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerWalletTxns() throws Exception {
        // Initialize the database
        insertedCustomerWalletTxn = customerWalletTxnRepository.saveAndFlush(customerWalletTxn);

        // Get all the customerWalletTxnList
        restCustomerWalletTxnMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerWalletTxn.getId().intValue())))
            .andExpect(jsonPath("$.[*].txnType").value(hasItem(DEFAULT_TXN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].balanceAfter").value(hasItem(sameNumber(DEFAULT_BALANCE_AFTER))))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerWalletTxnsWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerWalletTxnServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerWalletTxnMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerWalletTxnServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerWalletTxnsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerWalletTxnServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerWalletTxnMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(customerWalletTxnRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustomerWalletTxn() throws Exception {
        // Initialize the database
        insertedCustomerWalletTxn = customerWalletTxnRepository.saveAndFlush(customerWalletTxn);

        // Get the customerWalletTxn
        restCustomerWalletTxnMockMvc
            .perform(get(ENTITY_API_URL_ID, customerWalletTxn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerWalletTxn.getId().intValue()))
            .andExpect(jsonPath("$.txnType").value(DEFAULT_TXN_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.balanceAfter").value(sameNumber(DEFAULT_BALANCE_AFTER)))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCustomerWalletTxn() throws Exception {
        // Get the customerWalletTxn
        restCustomerWalletTxnMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerWalletTxn() throws Exception {
        // Initialize the database
        insertedCustomerWalletTxn = customerWalletTxnRepository.saveAndFlush(customerWalletTxn);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerWalletTxn
        CustomerWalletTxn updatedCustomerWalletTxn = customerWalletTxnRepository.findById(customerWalletTxn.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerWalletTxn are not directly saved in db
        em.detach(updatedCustomerWalletTxn);
        updatedCustomerWalletTxn
            .txnType(UPDATED_TXN_TYPE)
            .amount(UPDATED_AMOUNT)
            .balanceAfter(UPDATED_BALANCE_AFTER)
            .expiresAt(UPDATED_EXPIRES_AT)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT);
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(updatedCustomerWalletTxn);

        restCustomerWalletTxnMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerWalletTxnDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerWalletTxnDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWalletTxn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerWalletTxnToMatchAllProperties(updatedCustomerWalletTxn);
    }

    @Test
    @Transactional
    void putNonExistingCustomerWalletTxn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWalletTxn.setId(longCount.incrementAndGet());

        // Create the CustomerWalletTxn
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerWalletTxnMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerWalletTxnDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerWalletTxnDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWalletTxn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerWalletTxn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWalletTxn.setId(longCount.incrementAndGet());

        // Create the CustomerWalletTxn
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWalletTxnMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerWalletTxnDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWalletTxn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerWalletTxn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWalletTxn.setId(longCount.incrementAndGet());

        // Create the CustomerWalletTxn
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWalletTxnMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletTxnDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerWalletTxn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWalletTxnWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerWalletTxn = customerWalletTxnRepository.saveAndFlush(customerWalletTxn);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerWalletTxn using partial update
        CustomerWalletTxn partialUpdatedCustomerWalletTxn = new CustomerWalletTxn();
        partialUpdatedCustomerWalletTxn.setId(customerWalletTxn.getId());

        partialUpdatedCustomerWalletTxn
            .amount(UPDATED_AMOUNT)
            .balanceAfter(UPDATED_BALANCE_AFTER)
            .expiresAt(UPDATED_EXPIRES_AT)
            .createdAt(UPDATED_CREATED_AT);

        restCustomerWalletTxnMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerWalletTxn.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerWalletTxn))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWalletTxn in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerWalletTxnUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerWalletTxn, customerWalletTxn),
            getPersistedCustomerWalletTxn(customerWalletTxn)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerWalletTxnWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerWalletTxn = customerWalletTxnRepository.saveAndFlush(customerWalletTxn);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerWalletTxn using partial update
        CustomerWalletTxn partialUpdatedCustomerWalletTxn = new CustomerWalletTxn();
        partialUpdatedCustomerWalletTxn.setId(customerWalletTxn.getId());

        partialUpdatedCustomerWalletTxn
            .txnType(UPDATED_TXN_TYPE)
            .amount(UPDATED_AMOUNT)
            .balanceAfter(UPDATED_BALANCE_AFTER)
            .expiresAt(UPDATED_EXPIRES_AT)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT);

        restCustomerWalletTxnMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerWalletTxn.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerWalletTxn))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWalletTxn in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerWalletTxnUpdatableFieldsEquals(
            partialUpdatedCustomerWalletTxn,
            getPersistedCustomerWalletTxn(partialUpdatedCustomerWalletTxn)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustomerWalletTxn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWalletTxn.setId(longCount.incrementAndGet());

        // Create the CustomerWalletTxn
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerWalletTxnMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerWalletTxnDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerWalletTxnDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWalletTxn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerWalletTxn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWalletTxn.setId(longCount.incrementAndGet());

        // Create the CustomerWalletTxn
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWalletTxnMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerWalletTxnDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWalletTxn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerWalletTxn() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWalletTxn.setId(longCount.incrementAndGet());

        // Create the CustomerWalletTxn
        CustomerWalletTxnDTO customerWalletTxnDTO = customerWalletTxnMapper.toDto(customerWalletTxn);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWalletTxnMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerWalletTxnDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerWalletTxn in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerWalletTxn() throws Exception {
        // Initialize the database
        insertedCustomerWalletTxn = customerWalletTxnRepository.saveAndFlush(customerWalletTxn);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerWalletTxn
        restCustomerWalletTxnMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerWalletTxn.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerWalletTxnRepository.count();
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

    protected CustomerWalletTxn getPersistedCustomerWalletTxn(CustomerWalletTxn customerWalletTxn) {
        return customerWalletTxnRepository.findById(customerWalletTxn.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerWalletTxnToMatchAllProperties(CustomerWalletTxn expectedCustomerWalletTxn) {
        assertCustomerWalletTxnAllPropertiesEquals(expectedCustomerWalletTxn, getPersistedCustomerWalletTxn(expectedCustomerWalletTxn));
    }

    protected void assertPersistedCustomerWalletTxnToMatchUpdatableProperties(CustomerWalletTxn expectedCustomerWalletTxn) {
        assertCustomerWalletTxnAllUpdatablePropertiesEquals(
            expectedCustomerWalletTxn,
            getPersistedCustomerWalletTxn(expectedCustomerWalletTxn)
        );
    }
}

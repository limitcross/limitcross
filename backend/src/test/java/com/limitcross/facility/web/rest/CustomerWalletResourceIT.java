package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.CustomerWalletAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.CustomerWallet;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.repository.CustomerWalletRepository;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.service.CustomerWalletService;
import com.limitcross.facility.service.dto.CustomerWalletDTO;
import com.limitcross.facility.service.mapper.CustomerWalletMapper;
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
 * Integration tests for the {@link CustomerWalletResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerWalletResourceIT {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(1);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/customer-wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerWalletRepository customerWalletRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CustomerWalletRepository customerWalletRepositoryMock;

    @Autowired
    private CustomerWalletMapper customerWalletMapper;

    @Mock
    private CustomerWalletService customerWalletServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerWalletMockMvc;

    private CustomerWallet customerWallet;

    private CustomerWallet insertedCustomerWallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerWallet createEntity(EntityManager em) {
        CustomerWallet customerWallet = new CustomerWallet().balance(DEFAULT_BALANCE).updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        customerWallet.setUser(user);
        return customerWallet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerWallet createUpdatedEntity(EntityManager em) {
        CustomerWallet updatedCustomerWallet = new CustomerWallet().balance(UPDATED_BALANCE).updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCustomerWallet.setUser(user);
        return updatedCustomerWallet;
    }

    @BeforeEach
    void initTest() {
        customerWallet = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomerWallet != null) {
            customerWalletRepository.delete(insertedCustomerWallet);
            insertedCustomerWallet = null;
        }
    }

    @Test
    @Transactional
    void createCustomerWallet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerWallet
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);
        var returnedCustomerWalletDTO = om.readValue(
            restCustomerWalletMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerWalletDTO.class
        );

        // Validate the CustomerWallet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerWallet = customerWalletMapper.toEntity(returnedCustomerWalletDTO);
        assertCustomerWalletUpdatableFieldsEquals(returnedCustomerWallet, getPersistedCustomerWallet(returnedCustomerWallet));

        insertedCustomerWallet = returnedCustomerWallet;
    }

    @Test
    @Transactional
    void createCustomerWalletWithExistingId() throws Exception {
        // Create the CustomerWallet with an existing ID
        customerWallet.setId(1L);
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerWallet.setBalance(null);

        // Create the CustomerWallet, which fails.
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);

        restCustomerWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerWallets() throws Exception {
        // Initialize the database
        insertedCustomerWallet = customerWalletRepository.saveAndFlush(customerWallet);

        // Get all the customerWalletList
        restCustomerWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerWallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerWalletsWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerWalletServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerWalletMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerWalletServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomerWalletsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerWalletServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerWalletMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(customerWalletRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustomerWallet() throws Exception {
        // Initialize the database
        insertedCustomerWallet = customerWalletRepository.saveAndFlush(customerWallet);

        // Get the customerWallet
        restCustomerWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, customerWallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerWallet.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCustomerWallet() throws Exception {
        // Get the customerWallet
        restCustomerWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerWallet() throws Exception {
        // Initialize the database
        insertedCustomerWallet = customerWalletRepository.saveAndFlush(customerWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerWallet
        CustomerWallet updatedCustomerWallet = customerWalletRepository.findById(customerWallet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerWallet are not directly saved in db
        em.detach(updatedCustomerWallet);
        updatedCustomerWallet.balance(UPDATED_BALANCE).updatedAt(UPDATED_UPDATED_AT);
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(updatedCustomerWallet);

        restCustomerWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerWalletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerWalletDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerWalletToMatchAllProperties(updatedCustomerWallet);
    }

    @Test
    @Transactional
    void putNonExistingCustomerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWallet.setId(longCount.incrementAndGet());

        // Create the CustomerWallet
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerWalletDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWallet.setId(longCount.incrementAndGet());

        // Create the CustomerWallet
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWallet.setId(longCount.incrementAndGet());

        // Create the CustomerWallet
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerWalletDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWalletWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerWallet = customerWalletRepository.saveAndFlush(customerWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerWallet using partial update
        CustomerWallet partialUpdatedCustomerWallet = new CustomerWallet();
        partialUpdatedCustomerWallet.setId(customerWallet.getId());

        partialUpdatedCustomerWallet.updatedAt(UPDATED_UPDATED_AT);

        restCustomerWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerWallet))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerWalletUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerWallet, customerWallet),
            getPersistedCustomerWallet(customerWallet)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerWalletWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerWallet = customerWalletRepository.saveAndFlush(customerWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerWallet using partial update
        CustomerWallet partialUpdatedCustomerWallet = new CustomerWallet();
        partialUpdatedCustomerWallet.setId(customerWallet.getId());

        partialUpdatedCustomerWallet.balance(UPDATED_BALANCE).updatedAt(UPDATED_UPDATED_AT);

        restCustomerWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerWallet))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerWalletUpdatableFieldsEquals(partialUpdatedCustomerWallet, getPersistedCustomerWallet(partialUpdatedCustomerWallet));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWallet.setId(longCount.incrementAndGet());

        // Create the CustomerWallet
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerWalletDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWallet.setId(longCount.incrementAndGet());

        // Create the CustomerWallet
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerWalletDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerWallet.setId(longCount.incrementAndGet());

        // Create the CustomerWallet
        CustomerWalletDTO customerWalletDTO = customerWalletMapper.toDto(customerWallet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerWalletDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerWallet() throws Exception {
        // Initialize the database
        insertedCustomerWallet = customerWalletRepository.saveAndFlush(customerWallet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerWallet
        restCustomerWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerWallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerWalletRepository.count();
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

    protected CustomerWallet getPersistedCustomerWallet(CustomerWallet customerWallet) {
        return customerWalletRepository.findById(customerWallet.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerWalletToMatchAllProperties(CustomerWallet expectedCustomerWallet) {
        assertCustomerWalletAllPropertiesEquals(expectedCustomerWallet, getPersistedCustomerWallet(expectedCustomerWallet));
    }

    protected void assertPersistedCustomerWalletToMatchUpdatableProperties(CustomerWallet expectedCustomerWallet) {
        assertCustomerWalletAllUpdatablePropertiesEquals(expectedCustomerWallet, getPersistedCustomerWallet(expectedCustomerWallet));
    }
}

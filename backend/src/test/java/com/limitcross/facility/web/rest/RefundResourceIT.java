package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.RefundAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.Payment;
import com.limitcross.facility.domain.Refund;
import com.limitcross.facility.domain.enumeration.RefundStatus;
import com.limitcross.facility.repository.RefundRepository;
import com.limitcross.facility.service.RefundService;
import com.limitcross.facility.service.dto.RefundDTO;
import com.limitcross.facility.service.mapper.RefundMapper;
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
 * Integration tests for the {@link RefundResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RefundResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final RefundStatus DEFAULT_STATUS = RefundStatus.INITIATED;
    private static final RefundStatus UPDATED_STATUS = RefundStatus.PROCESSED;

    private static final String DEFAULT_GATEWAY_REFUND_ID = "AAAAAAAAAA";
    private static final String UPDATED_GATEWAY_REFUND_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/refunds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RefundRepository refundRepository;

    @Mock
    private RefundRepository refundRepositoryMock;

    @Autowired
    private RefundMapper refundMapper;

    @Mock
    private RefundService refundServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRefundMockMvc;

    private Refund refund;

    private Refund insertedRefund;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refund createEntity(EntityManager em) {
        Refund refund = new Refund()
            .amount(DEFAULT_AMOUNT)
            .reason(DEFAULT_REASON)
            .status(DEFAULT_STATUS)
            .gatewayRefundId(DEFAULT_GATEWAY_REFUND_ID)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        refund.setPayment(payment);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        refund.setBooking(booking);
        return refund;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refund createUpdatedEntity(EntityManager em) {
        Refund updatedRefund = new Refund()
            .amount(UPDATED_AMOUNT)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .gatewayRefundId(UPDATED_GATEWAY_REFUND_ID)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createUpdatedEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        updatedRefund.setPayment(payment);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedRefund.setBooking(booking);
        return updatedRefund;
    }

    @BeforeEach
    void initTest() {
        refund = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRefund != null) {
            refundRepository.delete(insertedRefund);
            insertedRefund = null;
        }
    }

    @Test
    @Transactional
    void createRefund() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);
        var returnedRefundDTO = om.readValue(
            restRefundMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RefundDTO.class
        );

        // Validate the Refund in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRefund = refundMapper.toEntity(returnedRefundDTO);
        assertRefundUpdatableFieldsEquals(returnedRefund, getPersistedRefund(returnedRefund));

        insertedRefund = returnedRefund;
    }

    @Test
    @Transactional
    void createRefundWithExistingId() throws Exception {
        // Create the Refund with an existing ID
        refund.setId(1L);
        RefundDTO refundDTO = refundMapper.toDto(refund);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refund.setAmount(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        refund.setStatus(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRefunds() throws Exception {
        // Initialize the database
        insertedRefund = refundRepository.saveAndFlush(refund);

        // Get all the refundList
        restRefundMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refund.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].gatewayRefundId").value(hasItem(DEFAULT_GATEWAY_REFUND_ID)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRefundsWithEagerRelationshipsIsEnabled() throws Exception {
        when(refundServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRefundMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(refundServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRefundsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(refundServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRefundMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(refundRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRefund() throws Exception {
        // Initialize the database
        insertedRefund = refundRepository.saveAndFlush(refund);

        // Get the refund
        restRefundMockMvc
            .perform(get(ENTITY_API_URL_ID, refund.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(refund.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.gatewayRefundId").value(DEFAULT_GATEWAY_REFUND_ID))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRefund() throws Exception {
        // Get the refund
        restRefundMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRefund() throws Exception {
        // Initialize the database
        insertedRefund = refundRepository.saveAndFlush(refund);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refund
        Refund updatedRefund = refundRepository.findById(refund.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRefund are not directly saved in db
        em.detach(updatedRefund);
        updatedRefund
            .amount(UPDATED_AMOUNT)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .gatewayRefundId(UPDATED_GATEWAY_REFUND_ID)
            .createdAt(UPDATED_CREATED_AT);
        RefundDTO refundDTO = refundMapper.toDto(updatedRefund);

        restRefundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refundDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundDTO))
            )
            .andExpect(status().isOk());

        // Validate the Refund in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRefundToMatchAllProperties(updatedRefund);
    }

    @Test
    @Transactional
    void putNonExistingRefund() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refund.setId(longCount.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refundDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRefund() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refund.setId(longCount.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(refundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRefund() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refund.setId(longCount.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(refundDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refund in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRefundWithPatch() throws Exception {
        // Initialize the database
        insertedRefund = refundRepository.saveAndFlush(refund);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refund using partial update
        Refund partialUpdatedRefund = new Refund();
        partialUpdatedRefund.setId(refund.getId());

        partialUpdatedRefund.createdAt(UPDATED_CREATED_AT);

        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefund.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRefund))
            )
            .andExpect(status().isOk());

        // Validate the Refund in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRefundUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRefund, refund), getPersistedRefund(refund));
    }

    @Test
    @Transactional
    void fullUpdateRefundWithPatch() throws Exception {
        // Initialize the database
        insertedRefund = refundRepository.saveAndFlush(refund);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the refund using partial update
        Refund partialUpdatedRefund = new Refund();
        partialUpdatedRefund.setId(refund.getId());

        partialUpdatedRefund
            .amount(UPDATED_AMOUNT)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .gatewayRefundId(UPDATED_GATEWAY_REFUND_ID)
            .createdAt(UPDATED_CREATED_AT);

        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefund.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRefund))
            )
            .andExpect(status().isOk());

        // Validate the Refund in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRefundUpdatableFieldsEquals(partialUpdatedRefund, getPersistedRefund(partialUpdatedRefund));
    }

    @Test
    @Transactional
    void patchNonExistingRefund() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refund.setId(longCount.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, refundDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(refundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRefund() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refund.setId(longCount.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(refundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRefund() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        refund.setId(longCount.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(refundDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refund in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRefund() throws Exception {
        // Initialize the database
        insertedRefund = refundRepository.saveAndFlush(refund);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the refund
        restRefundMockMvc
            .perform(delete(ENTITY_API_URL_ID, refund.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return refundRepository.count();
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

    protected Refund getPersistedRefund(Refund refund) {
        return refundRepository.findById(refund.getId()).orElseThrow();
    }

    protected void assertPersistedRefundToMatchAllProperties(Refund expectedRefund) {
        assertRefundAllPropertiesEquals(expectedRefund, getPersistedRefund(expectedRefund));
    }

    protected void assertPersistedRefundToMatchUpdatableProperties(Refund expectedRefund) {
        assertRefundAllUpdatablePropertiesEquals(expectedRefund, getPersistedRefund(expectedRefund));
    }
}

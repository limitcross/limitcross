package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.WarrantyClaimAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Booking;
import com.limitcross.facility.domain.User;
import com.limitcross.facility.domain.WarrantyClaim;
import com.limitcross.facility.domain.enumeration.WarrantyStatus;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.repository.WarrantyClaimRepository;
import com.limitcross.facility.service.WarrantyClaimService;
import com.limitcross.facility.service.dto.WarrantyClaimDTO;
import com.limitcross.facility.service.mapper.WarrantyClaimMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link WarrantyClaimResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WarrantyClaimResourceIT {

    private static final String DEFAULT_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE = "BBBBBBBBBB";

    private static final WarrantyStatus DEFAULT_STATUS = WarrantyStatus.RAISED;
    private static final WarrantyStatus UPDATED_STATUS = WarrantyStatus.APPROVED;

    private static final Boolean DEFAULT_WITHIN_WARRANTY = false;
    private static final Boolean UPDATED_WITHIN_WARRANTY = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RESOLVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESOLVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/warranty-claims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private WarrantyClaimRepository warrantyClaimRepositoryMock;

    @Autowired
    private WarrantyClaimMapper warrantyClaimMapper;

    @Mock
    private WarrantyClaimService warrantyClaimServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarrantyClaimMockMvc;

    private WarrantyClaim warrantyClaim;

    private WarrantyClaim insertedWarrantyClaim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarrantyClaim createEntity(EntityManager em) {
        WarrantyClaim warrantyClaim = new WarrantyClaim()
            .issue(DEFAULT_ISSUE)
            .status(DEFAULT_STATUS)
            .withinWarranty(DEFAULT_WITHIN_WARRANTY)
            .createdAt(DEFAULT_CREATED_AT)
            .resolvedAt(DEFAULT_RESOLVED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        warrantyClaim.setCustomer(user);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        warrantyClaim.setBooking(booking);
        return warrantyClaim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarrantyClaim createUpdatedEntity(EntityManager em) {
        WarrantyClaim updatedWarrantyClaim = new WarrantyClaim()
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS)
            .withinWarranty(UPDATED_WITHIN_WARRANTY)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedWarrantyClaim.setCustomer(user);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        updatedWarrantyClaim.setBooking(booking);
        return updatedWarrantyClaim;
    }

    @BeforeEach
    void initTest() {
        warrantyClaim = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedWarrantyClaim != null) {
            warrantyClaimRepository.delete(insertedWarrantyClaim);
            insertedWarrantyClaim = null;
        }
    }

    @Test
    @Transactional
    void createWarrantyClaim() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WarrantyClaim
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);
        var returnedWarrantyClaimDTO = om.readValue(
            restWarrantyClaimMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyClaimDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WarrantyClaimDTO.class
        );

        // Validate the WarrantyClaim in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWarrantyClaim = warrantyClaimMapper.toEntity(returnedWarrantyClaimDTO);
        assertWarrantyClaimUpdatableFieldsEquals(returnedWarrantyClaim, getPersistedWarrantyClaim(returnedWarrantyClaim));

        insertedWarrantyClaim = returnedWarrantyClaim;
    }

    @Test
    @Transactional
    void createWarrantyClaimWithExistingId() throws Exception {
        // Create the WarrantyClaim with an existing ID
        warrantyClaim.setId(1L);
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarrantyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyClaimDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WarrantyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIssueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        warrantyClaim.setIssue(null);

        // Create the WarrantyClaim, which fails.
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        restWarrantyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyClaimDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        warrantyClaim.setStatus(null);

        // Create the WarrantyClaim, which fails.
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        restWarrantyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyClaimDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWithinWarrantyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        warrantyClaim.setWithinWarranty(null);

        // Create the WarrantyClaim, which fails.
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        restWarrantyClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyClaimDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWarrantyClaims() throws Exception {
        // Initialize the database
        insertedWarrantyClaim = warrantyClaimRepository.saveAndFlush(warrantyClaim);

        // Get all the warrantyClaimList
        restWarrantyClaimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warrantyClaim.getId().intValue())))
            .andExpect(jsonPath("$.[*].issue").value(hasItem(DEFAULT_ISSUE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].withinWarranty").value(hasItem(DEFAULT_WITHIN_WARRANTY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].resolvedAt").value(hasItem(DEFAULT_RESOLVED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWarrantyClaimsWithEagerRelationshipsIsEnabled() throws Exception {
        when(warrantyClaimServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWarrantyClaimMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(warrantyClaimServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWarrantyClaimsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(warrantyClaimServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWarrantyClaimMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(warrantyClaimRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWarrantyClaim() throws Exception {
        // Initialize the database
        insertedWarrantyClaim = warrantyClaimRepository.saveAndFlush(warrantyClaim);

        // Get the warrantyClaim
        restWarrantyClaimMockMvc
            .perform(get(ENTITY_API_URL_ID, warrantyClaim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warrantyClaim.getId().intValue()))
            .andExpect(jsonPath("$.issue").value(DEFAULT_ISSUE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.withinWarranty").value(DEFAULT_WITHIN_WARRANTY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.resolvedAt").value(DEFAULT_RESOLVED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWarrantyClaim() throws Exception {
        // Get the warrantyClaim
        restWarrantyClaimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWarrantyClaim() throws Exception {
        // Initialize the database
        insertedWarrantyClaim = warrantyClaimRepository.saveAndFlush(warrantyClaim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warrantyClaim
        WarrantyClaim updatedWarrantyClaim = warrantyClaimRepository.findById(warrantyClaim.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWarrantyClaim are not directly saved in db
        em.detach(updatedWarrantyClaim);
        updatedWarrantyClaim
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS)
            .withinWarranty(UPDATED_WITHIN_WARRANTY)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(updatedWarrantyClaim);

        restWarrantyClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warrantyClaimDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warrantyClaimDTO))
            )
            .andExpect(status().isOk());

        // Validate the WarrantyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWarrantyClaimToMatchAllProperties(updatedWarrantyClaim);
    }

    @Test
    @Transactional
    void putNonExistingWarrantyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warrantyClaim.setId(longCount.incrementAndGet());

        // Create the WarrantyClaim
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarrantyClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warrantyClaimDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warrantyClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarrantyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warrantyClaim.setId(longCount.incrementAndGet());

        // Create the WarrantyClaim
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warrantyClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarrantyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warrantyClaim.setId(longCount.incrementAndGet());

        // Create the WarrantyClaim
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyClaimMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyClaimDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarrantyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarrantyClaimWithPatch() throws Exception {
        // Initialize the database
        insertedWarrantyClaim = warrantyClaimRepository.saveAndFlush(warrantyClaim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warrantyClaim using partial update
        WarrantyClaim partialUpdatedWarrantyClaim = new WarrantyClaim();
        partialUpdatedWarrantyClaim.setId(warrantyClaim.getId());

        partialUpdatedWarrantyClaim.status(UPDATED_STATUS);

        restWarrantyClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarrantyClaim.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWarrantyClaim))
            )
            .andExpect(status().isOk());

        // Validate the WarrantyClaim in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarrantyClaimUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWarrantyClaim, warrantyClaim),
            getPersistedWarrantyClaim(warrantyClaim)
        );
    }

    @Test
    @Transactional
    void fullUpdateWarrantyClaimWithPatch() throws Exception {
        // Initialize the database
        insertedWarrantyClaim = warrantyClaimRepository.saveAndFlush(warrantyClaim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warrantyClaim using partial update
        WarrantyClaim partialUpdatedWarrantyClaim = new WarrantyClaim();
        partialUpdatedWarrantyClaim.setId(warrantyClaim.getId());

        partialUpdatedWarrantyClaim
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS)
            .withinWarranty(UPDATED_WITHIN_WARRANTY)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT);

        restWarrantyClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarrantyClaim.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWarrantyClaim))
            )
            .andExpect(status().isOk());

        // Validate the WarrantyClaim in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarrantyClaimUpdatableFieldsEquals(partialUpdatedWarrantyClaim, getPersistedWarrantyClaim(partialUpdatedWarrantyClaim));
    }

    @Test
    @Transactional
    void patchNonExistingWarrantyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warrantyClaim.setId(longCount.incrementAndGet());

        // Create the WarrantyClaim
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarrantyClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warrantyClaimDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(warrantyClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarrantyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warrantyClaim.setId(longCount.incrementAndGet());

        // Create the WarrantyClaim
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(warrantyClaimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarrantyClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warrantyClaim.setId(longCount.incrementAndGet());

        // Create the WarrantyClaim
        WarrantyClaimDTO warrantyClaimDTO = warrantyClaimMapper.toDto(warrantyClaim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyClaimMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(warrantyClaimDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarrantyClaim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarrantyClaim() throws Exception {
        // Initialize the database
        insertedWarrantyClaim = warrantyClaimRepository.saveAndFlush(warrantyClaim);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the warrantyClaim
        restWarrantyClaimMockMvc
            .perform(delete(ENTITY_API_URL_ID, warrantyClaim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return warrantyClaimRepository.count();
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

    protected WarrantyClaim getPersistedWarrantyClaim(WarrantyClaim warrantyClaim) {
        return warrantyClaimRepository.findById(warrantyClaim.getId()).orElseThrow();
    }

    protected void assertPersistedWarrantyClaimToMatchAllProperties(WarrantyClaim expectedWarrantyClaim) {
        assertWarrantyClaimAllPropertiesEquals(expectedWarrantyClaim, getPersistedWarrantyClaim(expectedWarrantyClaim));
    }

    protected void assertPersistedWarrantyClaimToMatchUpdatableProperties(WarrantyClaim expectedWarrantyClaim) {
        assertWarrantyClaimAllUpdatablePropertiesEquals(expectedWarrantyClaim, getPersistedWarrantyClaim(expectedWarrantyClaim));
    }
}

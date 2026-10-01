package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.ProfessionalLocationLogAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.ProfessionalLocationLog;
import com.limitcross.facility.repository.ProfessionalLocationLogRepository;
import com.limitcross.facility.service.dto.ProfessionalLocationLogDTO;
import com.limitcross.facility.service.mapper.ProfessionalLocationLogMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ProfessionalLocationLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalLocationLogResourceIT {

    private static final Long DEFAULT_PROFESSIONAL_ID = 1L;
    private static final Long UPDATED_PROFESSIONAL_ID = 2L;

    private static final String DEFAULT_BOOKING_REF = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_REF = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_SPEED_KMPH = 1D;
    private static final Double UPDATED_SPEED_KMPH = 2D;

    private static final Integer DEFAULT_BATTERY_PCT = 0;
    private static final Integer UPDATED_BATTERY_PCT = 1;

    private static final Instant DEFAULT_RECORDED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECORDED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/professional-location-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalLocationLogRepository professionalLocationLogRepository;

    @Autowired
    private ProfessionalLocationLogMapper professionalLocationLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalLocationLogMockMvc;

    private ProfessionalLocationLog professionalLocationLog;

    private ProfessionalLocationLog insertedProfessionalLocationLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalLocationLog createEntity() {
        return new ProfessionalLocationLog()
            .professionalId(DEFAULT_PROFESSIONAL_ID)
            .bookingRef(DEFAULT_BOOKING_REF)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .speedKmph(DEFAULT_SPEED_KMPH)
            .batteryPct(DEFAULT_BATTERY_PCT)
            .recordedAt(DEFAULT_RECORDED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalLocationLog createUpdatedEntity() {
        return new ProfessionalLocationLog()
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .bookingRef(UPDATED_BOOKING_REF)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speedKmph(UPDATED_SPEED_KMPH)
            .batteryPct(UPDATED_BATTERY_PCT)
            .recordedAt(UPDATED_RECORDED_AT);
    }

    @BeforeEach
    void initTest() {
        professionalLocationLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalLocationLog != null) {
            professionalLocationLogRepository.delete(insertedProfessionalLocationLog);
            insertedProfessionalLocationLog = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalLocationLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalLocationLog
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);
        var returnedProfessionalLocationLogDTO = om.readValue(
            restProfessionalLocationLogMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalLocationLogDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalLocationLogDTO.class
        );

        // Validate the ProfessionalLocationLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalLocationLog = professionalLocationLogMapper.toEntity(returnedProfessionalLocationLogDTO);
        assertProfessionalLocationLogUpdatableFieldsEquals(
            returnedProfessionalLocationLog,
            getPersistedProfessionalLocationLog(returnedProfessionalLocationLog)
        );

        insertedProfessionalLocationLog = returnedProfessionalLocationLog;
    }

    @Test
    @Transactional
    void createProfessionalLocationLogWithExistingId() throws Exception {
        // Create the ProfessionalLocationLog with an existing ID
        professionalLocationLog.setId(1L);
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalLocationLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalLocationLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalLocationLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProfessionalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalLocationLog.setProfessionalId(null);

        // Create the ProfessionalLocationLog, which fails.
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        restProfessionalLocationLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalLocationLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalLocationLog.setLatitude(null);

        // Create the ProfessionalLocationLog, which fails.
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        restProfessionalLocationLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalLocationLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalLocationLog.setLongitude(null);

        // Create the ProfessionalLocationLog, which fails.
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        restProfessionalLocationLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalLocationLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecordedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        professionalLocationLog.setRecordedAt(null);

        // Create the ProfessionalLocationLog, which fails.
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        restProfessionalLocationLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalLocationLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfessionalLocationLogs() throws Exception {
        // Initialize the database
        insertedProfessionalLocationLog = professionalLocationLogRepository.saveAndFlush(professionalLocationLog);

        // Get all the professionalLocationLogList
        restProfessionalLocationLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalLocationLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].professionalId").value(hasItem(DEFAULT_PROFESSIONAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].bookingRef").value(hasItem(DEFAULT_BOOKING_REF)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].speedKmph").value(hasItem(DEFAULT_SPEED_KMPH)))
            .andExpect(jsonPath("$.[*].batteryPct").value(hasItem(DEFAULT_BATTERY_PCT)))
            .andExpect(jsonPath("$.[*].recordedAt").value(hasItem(DEFAULT_RECORDED_AT.toString())));
    }

    @Test
    @Transactional
    void getProfessionalLocationLog() throws Exception {
        // Initialize the database
        insertedProfessionalLocationLog = professionalLocationLogRepository.saveAndFlush(professionalLocationLog);

        // Get the professionalLocationLog
        restProfessionalLocationLogMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalLocationLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalLocationLog.getId().intValue()))
            .andExpect(jsonPath("$.professionalId").value(DEFAULT_PROFESSIONAL_ID.intValue()))
            .andExpect(jsonPath("$.bookingRef").value(DEFAULT_BOOKING_REF))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.speedKmph").value(DEFAULT_SPEED_KMPH))
            .andExpect(jsonPath("$.batteryPct").value(DEFAULT_BATTERY_PCT))
            .andExpect(jsonPath("$.recordedAt").value(DEFAULT_RECORDED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalLocationLog() throws Exception {
        // Get the professionalLocationLog
        restProfessionalLocationLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalLocationLog() throws Exception {
        // Initialize the database
        insertedProfessionalLocationLog = professionalLocationLogRepository.saveAndFlush(professionalLocationLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalLocationLog
        ProfessionalLocationLog updatedProfessionalLocationLog = professionalLocationLogRepository
            .findById(professionalLocationLog.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalLocationLog are not directly saved in db
        em.detach(updatedProfessionalLocationLog);
        updatedProfessionalLocationLog
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .bookingRef(UPDATED_BOOKING_REF)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speedKmph(UPDATED_SPEED_KMPH)
            .batteryPct(UPDATED_BATTERY_PCT)
            .recordedAt(UPDATED_RECORDED_AT);
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(updatedProfessionalLocationLog);

        restProfessionalLocationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalLocationLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalLocationLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalLocationLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalLocationLogToMatchAllProperties(updatedProfessionalLocationLog);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalLocationLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalLocationLog.setId(longCount.incrementAndGet());

        // Create the ProfessionalLocationLog
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalLocationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalLocationLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalLocationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalLocationLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalLocationLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalLocationLog.setId(longCount.incrementAndGet());

        // Create the ProfessionalLocationLog
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalLocationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalLocationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalLocationLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalLocationLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalLocationLog.setId(longCount.incrementAndGet());

        // Create the ProfessionalLocationLog
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalLocationLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalLocationLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalLocationLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalLocationLogWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalLocationLog = professionalLocationLogRepository.saveAndFlush(professionalLocationLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalLocationLog using partial update
        ProfessionalLocationLog partialUpdatedProfessionalLocationLog = new ProfessionalLocationLog();
        partialUpdatedProfessionalLocationLog.setId(professionalLocationLog.getId());

        partialUpdatedProfessionalLocationLog
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speedKmph(UPDATED_SPEED_KMPH)
            .recordedAt(UPDATED_RECORDED_AT);

        restProfessionalLocationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalLocationLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalLocationLog))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalLocationLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalLocationLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalLocationLog, professionalLocationLog),
            getPersistedProfessionalLocationLog(professionalLocationLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalLocationLogWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalLocationLog = professionalLocationLogRepository.saveAndFlush(professionalLocationLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalLocationLog using partial update
        ProfessionalLocationLog partialUpdatedProfessionalLocationLog = new ProfessionalLocationLog();
        partialUpdatedProfessionalLocationLog.setId(professionalLocationLog.getId());

        partialUpdatedProfessionalLocationLog
            .professionalId(UPDATED_PROFESSIONAL_ID)
            .bookingRef(UPDATED_BOOKING_REF)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .speedKmph(UPDATED_SPEED_KMPH)
            .batteryPct(UPDATED_BATTERY_PCT)
            .recordedAt(UPDATED_RECORDED_AT);

        restProfessionalLocationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalLocationLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalLocationLog))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalLocationLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalLocationLogUpdatableFieldsEquals(
            partialUpdatedProfessionalLocationLog,
            getPersistedProfessionalLocationLog(partialUpdatedProfessionalLocationLog)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalLocationLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalLocationLog.setId(longCount.incrementAndGet());

        // Create the ProfessionalLocationLog
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalLocationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalLocationLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalLocationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalLocationLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalLocationLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalLocationLog.setId(longCount.incrementAndGet());

        // Create the ProfessionalLocationLog
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalLocationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalLocationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalLocationLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalLocationLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalLocationLog.setId(longCount.incrementAndGet());

        // Create the ProfessionalLocationLog
        ProfessionalLocationLogDTO professionalLocationLogDTO = professionalLocationLogMapper.toDto(professionalLocationLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalLocationLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalLocationLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalLocationLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalLocationLog() throws Exception {
        // Initialize the database
        insertedProfessionalLocationLog = professionalLocationLogRepository.saveAndFlush(professionalLocationLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalLocationLog
        restProfessionalLocationLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalLocationLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalLocationLogRepository.count();
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

    protected ProfessionalLocationLog getPersistedProfessionalLocationLog(ProfessionalLocationLog professionalLocationLog) {
        return professionalLocationLogRepository.findById(professionalLocationLog.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalLocationLogToMatchAllProperties(ProfessionalLocationLog expectedProfessionalLocationLog) {
        assertProfessionalLocationLogAllPropertiesEquals(
            expectedProfessionalLocationLog,
            getPersistedProfessionalLocationLog(expectedProfessionalLocationLog)
        );
    }

    protected void assertPersistedProfessionalLocationLogToMatchUpdatableProperties(
        ProfessionalLocationLog expectedProfessionalLocationLog
    ) {
        assertProfessionalLocationLogAllUpdatablePropertiesEquals(
            expectedProfessionalLocationLog,
            getPersistedProfessionalLocationLog(expectedProfessionalLocationLog)
        );
    }
}

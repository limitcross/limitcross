package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.SearchKeywordAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.FacilityService;
import com.limitcross.facility.domain.SearchKeyword;
import com.limitcross.facility.repository.SearchKeywordRepository;
import com.limitcross.facility.service.SearchKeywordService;
import com.limitcross.facility.service.dto.SearchKeywordDTO;
import com.limitcross.facility.service.mapper.SearchKeywordMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SearchKeywordResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SearchKeywordResourceIT {

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final String ENTITY_API_URL = "/api/search-keywords";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchKeywordRepository searchKeywordRepository;

    @Mock
    private SearchKeywordRepository searchKeywordRepositoryMock;

    @Autowired
    private SearchKeywordMapper searchKeywordMapper;

    @Mock
    private SearchKeywordService searchKeywordServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchKeywordMockMvc;

    private SearchKeyword searchKeyword;

    private SearchKeyword insertedSearchKeyword;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchKeyword createEntity(EntityManager em) {
        SearchKeyword searchKeyword = new SearchKeyword().keyword(DEFAULT_KEYWORD).weight(DEFAULT_WEIGHT);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        searchKeyword.setService(facilityService);
        return searchKeyword;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchKeyword createUpdatedEntity(EntityManager em) {
        SearchKeyword updatedSearchKeyword = new SearchKeyword().keyword(UPDATED_KEYWORD).weight(UPDATED_WEIGHT);
        // Add required entity
        FacilityService facilityService;
        if (TestUtil.findAll(em, FacilityService.class).isEmpty()) {
            facilityService = FacilityServiceResourceIT.createUpdatedEntity(em);
            em.persist(facilityService);
            em.flush();
        } else {
            facilityService = TestUtil.findAll(em, FacilityService.class).get(0);
        }
        updatedSearchKeyword.setService(facilityService);
        return updatedSearchKeyword;
    }

    @BeforeEach
    void initTest() {
        searchKeyword = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSearchKeyword != null) {
            searchKeywordRepository.delete(insertedSearchKeyword);
            insertedSearchKeyword = null;
        }
    }

    @Test
    @Transactional
    void createSearchKeyword() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SearchKeyword
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);
        var returnedSearchKeywordDTO = om.readValue(
            restSearchKeywordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchKeywordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SearchKeywordDTO.class
        );

        // Validate the SearchKeyword in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSearchKeyword = searchKeywordMapper.toEntity(returnedSearchKeywordDTO);
        assertSearchKeywordUpdatableFieldsEquals(returnedSearchKeyword, getPersistedSearchKeyword(returnedSearchKeyword));

        insertedSearchKeyword = returnedSearchKeyword;
    }

    @Test
    @Transactional
    void createSearchKeywordWithExistingId() throws Exception {
        // Create the SearchKeyword with an existing ID
        searchKeyword.setId(1L);
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchKeywordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchKeywordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SearchKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeywordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        searchKeyword.setKeyword(null);

        // Create the SearchKeyword, which fails.
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);

        restSearchKeywordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchKeywordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSearchKeywords() throws Exception {
        // Initialize the database
        insertedSearchKeyword = searchKeywordRepository.saveAndFlush(searchKeyword);

        // Get all the searchKeywordList
        restSearchKeywordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchKeyword.getId().intValue())))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSearchKeywordsWithEagerRelationshipsIsEnabled() throws Exception {
        when(searchKeywordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSearchKeywordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(searchKeywordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSearchKeywordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(searchKeywordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSearchKeywordMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(searchKeywordRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSearchKeyword() throws Exception {
        // Initialize the database
        insertedSearchKeyword = searchKeywordRepository.saveAndFlush(searchKeyword);

        // Get the searchKeyword
        restSearchKeywordMockMvc
            .perform(get(ENTITY_API_URL_ID, searchKeyword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchKeyword.getId().intValue()))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    void getNonExistingSearchKeyword() throws Exception {
        // Get the searchKeyword
        restSearchKeywordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchKeyword() throws Exception {
        // Initialize the database
        insertedSearchKeyword = searchKeywordRepository.saveAndFlush(searchKeyword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchKeyword
        SearchKeyword updatedSearchKeyword = searchKeywordRepository.findById(searchKeyword.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchKeyword are not directly saved in db
        em.detach(updatedSearchKeyword);
        updatedSearchKeyword.keyword(UPDATED_KEYWORD).weight(UPDATED_WEIGHT);
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(updatedSearchKeyword);

        restSearchKeywordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchKeywordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchKeywordDTO))
            )
            .andExpect(status().isOk());

        // Validate the SearchKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSearchKeywordToMatchAllProperties(updatedSearchKeyword);
    }

    @Test
    @Transactional
    void putNonExistingSearchKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchKeyword.setId(longCount.incrementAndGet());

        // Create the SearchKeyword
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchKeywordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchKeywordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchKeywordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchKeyword.setId(longCount.incrementAndGet());

        // Create the SearchKeyword
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchKeywordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchKeywordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchKeyword.setId(longCount.incrementAndGet());

        // Create the SearchKeyword
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchKeywordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchKeywordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchKeywordWithPatch() throws Exception {
        // Initialize the database
        insertedSearchKeyword = searchKeywordRepository.saveAndFlush(searchKeyword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchKeyword using partial update
        SearchKeyword partialUpdatedSearchKeyword = new SearchKeyword();
        partialUpdatedSearchKeyword.setId(searchKeyword.getId());

        partialUpdatedSearchKeyword.keyword(UPDATED_KEYWORD).weight(UPDATED_WEIGHT);

        restSearchKeywordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchKeyword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchKeyword))
            )
            .andExpect(status().isOk());

        // Validate the SearchKeyword in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchKeywordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSearchKeyword, searchKeyword),
            getPersistedSearchKeyword(searchKeyword)
        );
    }

    @Test
    @Transactional
    void fullUpdateSearchKeywordWithPatch() throws Exception {
        // Initialize the database
        insertedSearchKeyword = searchKeywordRepository.saveAndFlush(searchKeyword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchKeyword using partial update
        SearchKeyword partialUpdatedSearchKeyword = new SearchKeyword();
        partialUpdatedSearchKeyword.setId(searchKeyword.getId());

        partialUpdatedSearchKeyword.keyword(UPDATED_KEYWORD).weight(UPDATED_WEIGHT);

        restSearchKeywordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchKeyword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchKeyword))
            )
            .andExpect(status().isOk());

        // Validate the SearchKeyword in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchKeywordUpdatableFieldsEquals(partialUpdatedSearchKeyword, getPersistedSearchKeyword(partialUpdatedSearchKeyword));
    }

    @Test
    @Transactional
    void patchNonExistingSearchKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchKeyword.setId(longCount.incrementAndGet());

        // Create the SearchKeyword
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchKeywordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchKeywordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchKeywordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchKeyword.setId(longCount.incrementAndGet());

        // Create the SearchKeyword
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchKeywordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchKeywordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchKeyword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchKeyword.setId(longCount.incrementAndGet());

        // Create the SearchKeyword
        SearchKeywordDTO searchKeywordDTO = searchKeywordMapper.toDto(searchKeyword);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchKeywordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(searchKeywordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchKeyword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearchKeyword() throws Exception {
        // Initialize the database
        insertedSearchKeyword = searchKeywordRepository.saveAndFlush(searchKeyword);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the searchKeyword
        restSearchKeywordMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchKeyword.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return searchKeywordRepository.count();
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

    protected SearchKeyword getPersistedSearchKeyword(SearchKeyword searchKeyword) {
        return searchKeywordRepository.findById(searchKeyword.getId()).orElseThrow();
    }

    protected void assertPersistedSearchKeywordToMatchAllProperties(SearchKeyword expectedSearchKeyword) {
        assertSearchKeywordAllPropertiesEquals(expectedSearchKeyword, getPersistedSearchKeyword(expectedSearchKeyword));
    }

    protected void assertPersistedSearchKeywordToMatchUpdatableProperties(SearchKeyword expectedSearchKeyword) {
        assertSearchKeywordAllUpdatablePropertiesEquals(expectedSearchKeyword, getPersistedSearchKeyword(expectedSearchKeyword));
    }
}

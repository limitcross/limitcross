package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BannerAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.Banner;
import com.limitcross.facility.domain.enumeration.BannerPlacement;
import com.limitcross.facility.repository.BannerRepository;
import com.limitcross.facility.service.BannerService;
import com.limitcross.facility.service.dto.BannerDTO;
import com.limitcross.facility.service.mapper.BannerMapper;
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
 * Integration tests for the {@link BannerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BannerResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DEEP_LINK = "AAAAAAAAAA";
    private static final String UPDATED_DEEP_LINK = "BBBBBBBBBB";

    private static final BannerPlacement DEFAULT_PLACEMENT = BannerPlacement.HOME_TOP;
    private static final BannerPlacement UPDATED_PLACEMENT = BannerPlacement.HOME_MID;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final Instant DEFAULT_STARTS_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTS_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENDS_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDS_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/banners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BannerRepository bannerRepository;

    @Mock
    private BannerRepository bannerRepositoryMock;

    @Autowired
    private BannerMapper bannerMapper;

    @Mock
    private BannerService bannerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBannerMockMvc;

    private Banner banner;

    private Banner insertedBanner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banner createEntity() {
        return new Banner()
            .title(DEFAULT_TITLE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .deepLink(DEFAULT_DEEP_LINK)
            .placement(DEFAULT_PLACEMENT)
            .sortOrder(DEFAULT_SORT_ORDER)
            .startsAt(DEFAULT_STARTS_AT)
            .endsAt(DEFAULT_ENDS_AT)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banner createUpdatedEntity() {
        return new Banner()
            .title(UPDATED_TITLE)
            .imageUrl(UPDATED_IMAGE_URL)
            .deepLink(UPDATED_DEEP_LINK)
            .placement(UPDATED_PLACEMENT)
            .sortOrder(UPDATED_SORT_ORDER)
            .startsAt(UPDATED_STARTS_AT)
            .endsAt(UPDATED_ENDS_AT)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        banner = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBanner != null) {
            bannerRepository.delete(insertedBanner);
            insertedBanner = null;
        }
    }

    @Test
    @Transactional
    void createBanner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);
        var returnedBannerDTO = om.readValue(
            restBannerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BannerDTO.class
        );

        // Validate the Banner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBanner = bannerMapper.toEntity(returnedBannerDTO);
        assertBannerUpdatableFieldsEquals(returnedBanner, getPersistedBanner(returnedBanner));

        insertedBanner = returnedBanner;
    }

    @Test
    @Transactional
    void createBannerWithExistingId() throws Exception {
        // Create the Banner with an existing ID
        banner.setId(1L);
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBannerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        banner.setTitle(null);

        // Create the Banner, which fails.
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        restBannerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        banner.setImageUrl(null);

        // Create the Banner, which fails.
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        restBannerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlacementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        banner.setPlacement(null);

        // Create the Banner, which fails.
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        restBannerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartsAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        banner.setStartsAt(null);

        // Create the Banner, which fails.
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        restBannerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        banner.setActive(null);

        // Create the Banner, which fails.
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        restBannerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBanners() throws Exception {
        // Initialize the database
        insertedBanner = bannerRepository.saveAndFlush(banner);

        // Get all the bannerList
        restBannerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banner.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].deepLink").value(hasItem(DEFAULT_DEEP_LINK)))
            .andExpect(jsonPath("$.[*].placement").value(hasItem(DEFAULT_PLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].startsAt").value(hasItem(DEFAULT_STARTS_AT.toString())))
            .andExpect(jsonPath("$.[*].endsAt").value(hasItem(DEFAULT_ENDS_AT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBannersWithEagerRelationshipsIsEnabled() throws Exception {
        when(bannerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBannerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bannerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBannersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bannerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBannerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bannerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBanner() throws Exception {
        // Initialize the database
        insertedBanner = bannerRepository.saveAndFlush(banner);

        // Get the banner
        restBannerMockMvc
            .perform(get(ENTITY_API_URL_ID, banner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(banner.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.deepLink").value(DEFAULT_DEEP_LINK))
            .andExpect(jsonPath("$.placement").value(DEFAULT_PLACEMENT.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.startsAt").value(DEFAULT_STARTS_AT.toString()))
            .andExpect(jsonPath("$.endsAt").value(DEFAULT_ENDS_AT.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingBanner() throws Exception {
        // Get the banner
        restBannerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBanner() throws Exception {
        // Initialize the database
        insertedBanner = bannerRepository.saveAndFlush(banner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the banner
        Banner updatedBanner = bannerRepository.findById(banner.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBanner are not directly saved in db
        em.detach(updatedBanner);
        updatedBanner
            .title(UPDATED_TITLE)
            .imageUrl(UPDATED_IMAGE_URL)
            .deepLink(UPDATED_DEEP_LINK)
            .placement(UPDATED_PLACEMENT)
            .sortOrder(UPDATED_SORT_ORDER)
            .startsAt(UPDATED_STARTS_AT)
            .endsAt(UPDATED_ENDS_AT)
            .active(UPDATED_ACTIVE);
        BannerDTO bannerDTO = bannerMapper.toDto(updatedBanner);

        restBannerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bannerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Banner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBannerToMatchAllProperties(updatedBanner);
    }

    @Test
    @Transactional
    void putNonExistingBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        banner.setId(longCount.incrementAndGet());

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBannerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bannerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        banner.setId(longCount.incrementAndGet());

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bannerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        banner.setId(longCount.incrementAndGet());

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bannerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Banner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBannerWithPatch() throws Exception {
        // Initialize the database
        insertedBanner = bannerRepository.saveAndFlush(banner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the banner using partial update
        Banner partialUpdatedBanner = new Banner();
        partialUpdatedBanner.setId(banner.getId());

        partialUpdatedBanner.title(UPDATED_TITLE).imageUrl(UPDATED_IMAGE_URL).sortOrder(UPDATED_SORT_ORDER);

        restBannerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBanner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBanner))
            )
            .andExpect(status().isOk());

        // Validate the Banner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBannerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBanner, banner), getPersistedBanner(banner));
    }

    @Test
    @Transactional
    void fullUpdateBannerWithPatch() throws Exception {
        // Initialize the database
        insertedBanner = bannerRepository.saveAndFlush(banner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the banner using partial update
        Banner partialUpdatedBanner = new Banner();
        partialUpdatedBanner.setId(banner.getId());

        partialUpdatedBanner
            .title(UPDATED_TITLE)
            .imageUrl(UPDATED_IMAGE_URL)
            .deepLink(UPDATED_DEEP_LINK)
            .placement(UPDATED_PLACEMENT)
            .sortOrder(UPDATED_SORT_ORDER)
            .startsAt(UPDATED_STARTS_AT)
            .endsAt(UPDATED_ENDS_AT)
            .active(UPDATED_ACTIVE);

        restBannerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBanner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBanner))
            )
            .andExpect(status().isOk());

        // Validate the Banner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBannerUpdatableFieldsEquals(partialUpdatedBanner, getPersistedBanner(partialUpdatedBanner));
    }

    @Test
    @Transactional
    void patchNonExistingBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        banner.setId(longCount.incrementAndGet());

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBannerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bannerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bannerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        banner.setId(longCount.incrementAndGet());

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bannerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        banner.setId(longCount.incrementAndGet());

        // Create the Banner
        BannerDTO bannerDTO = bannerMapper.toDto(banner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bannerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Banner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBanner() throws Exception {
        // Initialize the database
        insertedBanner = bannerRepository.saveAndFlush(banner);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the banner
        restBannerMockMvc
            .perform(delete(ENTITY_API_URL_ID, banner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bannerRepository.count();
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

    protected Banner getPersistedBanner(Banner banner) {
        return bannerRepository.findById(banner.getId()).orElseThrow();
    }

    protected void assertPersistedBannerToMatchAllProperties(Banner expectedBanner) {
        assertBannerAllPropertiesEquals(expectedBanner, getPersistedBanner(expectedBanner));
    }

    protected void assertPersistedBannerToMatchUpdatableProperties(Banner expectedBanner) {
        assertBannerAllUpdatablePropertiesEquals(expectedBanner, getPersistedBanner(expectedBanner));
    }
}

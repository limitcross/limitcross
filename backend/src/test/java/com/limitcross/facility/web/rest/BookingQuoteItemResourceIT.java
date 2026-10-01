package com.limitcross.facility.web.rest;

import static com.limitcross.facility.domain.BookingQuoteItemAsserts.*;
import static com.limitcross.facility.web.rest.TestUtil.createUpdateProxyForBean;
import static com.limitcross.facility.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limitcross.facility.IntegrationTest;
import com.limitcross.facility.domain.BookingQuote;
import com.limitcross.facility.domain.BookingQuoteItem;
import com.limitcross.facility.domain.enumeration.QuoteItemType;
import com.limitcross.facility.repository.BookingQuoteItemRepository;
import com.limitcross.facility.service.dto.BookingQuoteItemDTO;
import com.limitcross.facility.service.mapper.BookingQuoteItemMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link BookingQuoteItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookingQuoteItemResourceIT {

    private static final QuoteItemType DEFAULT_ITEM_TYPE = QuoteItemType.LABOUR;
    private static final QuoteItemType UPDATED_ITEM_TYPE = QuoteItemType.MATERIAL;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(0);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(1);

    private static final BigDecimal DEFAULT_LINE_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_LINE_TOTAL = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/booking-quote-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookingQuoteItemRepository bookingQuoteItemRepository;

    @Autowired
    private BookingQuoteItemMapper bookingQuoteItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingQuoteItemMockMvc;

    private BookingQuoteItem bookingQuoteItem;

    private BookingQuoteItem insertedBookingQuoteItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingQuoteItem createEntity(EntityManager em) {
        BookingQuoteItem bookingQuoteItem = new BookingQuoteItem()
            .itemType(DEFAULT_ITEM_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .lineTotal(DEFAULT_LINE_TOTAL);
        // Add required entity
        BookingQuote bookingQuote;
        if (TestUtil.findAll(em, BookingQuote.class).isEmpty()) {
            bookingQuote = BookingQuoteResourceIT.createEntity(em);
            em.persist(bookingQuote);
            em.flush();
        } else {
            bookingQuote = TestUtil.findAll(em, BookingQuote.class).get(0);
        }
        bookingQuoteItem.setQuote(bookingQuote);
        return bookingQuoteItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookingQuoteItem createUpdatedEntity(EntityManager em) {
        BookingQuoteItem updatedBookingQuoteItem = new BookingQuoteItem()
            .itemType(UPDATED_ITEM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .lineTotal(UPDATED_LINE_TOTAL);
        // Add required entity
        BookingQuote bookingQuote;
        if (TestUtil.findAll(em, BookingQuote.class).isEmpty()) {
            bookingQuote = BookingQuoteResourceIT.createUpdatedEntity(em);
            em.persist(bookingQuote);
            em.flush();
        } else {
            bookingQuote = TestUtil.findAll(em, BookingQuote.class).get(0);
        }
        updatedBookingQuoteItem.setQuote(bookingQuote);
        return updatedBookingQuoteItem;
    }

    @BeforeEach
    void initTest() {
        bookingQuoteItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBookingQuoteItem != null) {
            bookingQuoteItemRepository.delete(insertedBookingQuoteItem);
            insertedBookingQuoteItem = null;
        }
    }

    @Test
    @Transactional
    void createBookingQuoteItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BookingQuoteItem
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);
        var returnedBookingQuoteItemDTO = om.readValue(
            restBookingQuoteItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookingQuoteItemDTO.class
        );

        // Validate the BookingQuoteItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookingQuoteItem = bookingQuoteItemMapper.toEntity(returnedBookingQuoteItemDTO);
        assertBookingQuoteItemUpdatableFieldsEquals(returnedBookingQuoteItem, getPersistedBookingQuoteItem(returnedBookingQuoteItem));

        insertedBookingQuoteItem = returnedBookingQuoteItem;
    }

    @Test
    @Transactional
    void createBookingQuoteItemWithExistingId() throws Exception {
        // Create the BookingQuoteItem with an existing ID
        bookingQuoteItem.setId(1L);
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingQuoteItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookingQuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkItemTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingQuoteItem.setItemType(null);

        // Create the BookingQuoteItem, which fails.
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        restBookingQuoteItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingQuoteItem.setDescription(null);

        // Create the BookingQuoteItem, which fails.
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        restBookingQuoteItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingQuoteItem.setUnitPrice(null);

        // Create the BookingQuoteItem, which fails.
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        restBookingQuoteItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingQuoteItem.setQuantity(null);

        // Create the BookingQuoteItem, which fails.
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        restBookingQuoteItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLineTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookingQuoteItem.setLineTotal(null);

        // Create the BookingQuoteItem, which fails.
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        restBookingQuoteItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookingQuoteItems() throws Exception {
        // Initialize the database
        insertedBookingQuoteItem = bookingQuoteItemRepository.saveAndFlush(bookingQuoteItem);

        // Get all the bookingQuoteItemList
        restBookingQuoteItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingQuoteItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemType").value(hasItem(DEFAULT_ITEM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].lineTotal").value(hasItem(sameNumber(DEFAULT_LINE_TOTAL))));
    }

    @Test
    @Transactional
    void getBookingQuoteItem() throws Exception {
        // Initialize the database
        insertedBookingQuoteItem = bookingQuoteItemRepository.saveAndFlush(bookingQuoteItem);

        // Get the bookingQuoteItem
        restBookingQuoteItemMockMvc
            .perform(get(ENTITY_API_URL_ID, bookingQuoteItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookingQuoteItem.getId().intValue()))
            .andExpect(jsonPath("$.itemType").value(DEFAULT_ITEM_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.lineTotal").value(sameNumber(DEFAULT_LINE_TOTAL)));
    }

    @Test
    @Transactional
    void getNonExistingBookingQuoteItem() throws Exception {
        // Get the bookingQuoteItem
        restBookingQuoteItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookingQuoteItem() throws Exception {
        // Initialize the database
        insertedBookingQuoteItem = bookingQuoteItemRepository.saveAndFlush(bookingQuoteItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingQuoteItem
        BookingQuoteItem updatedBookingQuoteItem = bookingQuoteItemRepository.findById(bookingQuoteItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookingQuoteItem are not directly saved in db
        em.detach(updatedBookingQuoteItem);
        updatedBookingQuoteItem
            .itemType(UPDATED_ITEM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .lineTotal(UPDATED_LINE_TOTAL);
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(updatedBookingQuoteItem);

        restBookingQuoteItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingQuoteItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingQuoteItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the BookingQuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookingQuoteItemToMatchAllProperties(updatedBookingQuoteItem);
    }

    @Test
    @Transactional
    void putNonExistingBookingQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuoteItem.setId(longCount.incrementAndGet());

        // Create the BookingQuoteItem
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingQuoteItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingQuoteItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingQuoteItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingQuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookingQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuoteItem.setId(longCount.incrementAndGet());

        // Create the BookingQuoteItem
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingQuoteItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookingQuoteItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingQuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookingQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuoteItem.setId(longCount.incrementAndGet());

        // Create the BookingQuoteItem
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingQuoteItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookingQuoteItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingQuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingQuoteItemWithPatch() throws Exception {
        // Initialize the database
        insertedBookingQuoteItem = bookingQuoteItemRepository.saveAndFlush(bookingQuoteItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingQuoteItem using partial update
        BookingQuoteItem partialUpdatedBookingQuoteItem = new BookingQuoteItem();
        partialUpdatedBookingQuoteItem.setId(bookingQuoteItem.getId());

        partialUpdatedBookingQuoteItem.description(UPDATED_DESCRIPTION);

        restBookingQuoteItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingQuoteItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingQuoteItem))
            )
            .andExpect(status().isOk());

        // Validate the BookingQuoteItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingQuoteItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBookingQuoteItem, bookingQuoteItem),
            getPersistedBookingQuoteItem(bookingQuoteItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateBookingQuoteItemWithPatch() throws Exception {
        // Initialize the database
        insertedBookingQuoteItem = bookingQuoteItemRepository.saveAndFlush(bookingQuoteItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookingQuoteItem using partial update
        BookingQuoteItem partialUpdatedBookingQuoteItem = new BookingQuoteItem();
        partialUpdatedBookingQuoteItem.setId(bookingQuoteItem.getId());

        partialUpdatedBookingQuoteItem
            .itemType(UPDATED_ITEM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY)
            .lineTotal(UPDATED_LINE_TOTAL);

        restBookingQuoteItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookingQuoteItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookingQuoteItem))
            )
            .andExpect(status().isOk());

        // Validate the BookingQuoteItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookingQuoteItemUpdatableFieldsEquals(
            partialUpdatedBookingQuoteItem,
            getPersistedBookingQuoteItem(partialUpdatedBookingQuoteItem)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBookingQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuoteItem.setId(longCount.incrementAndGet());

        // Create the BookingQuoteItem
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingQuoteItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingQuoteItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingQuoteItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingQuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookingQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuoteItem.setId(longCount.incrementAndGet());

        // Create the BookingQuoteItem
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingQuoteItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookingQuoteItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookingQuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookingQuoteItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookingQuoteItem.setId(longCount.incrementAndGet());

        // Create the BookingQuoteItem
        BookingQuoteItemDTO bookingQuoteItemDTO = bookingQuoteItemMapper.toDto(bookingQuoteItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingQuoteItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookingQuoteItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookingQuoteItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookingQuoteItem() throws Exception {
        // Initialize the database
        insertedBookingQuoteItem = bookingQuoteItemRepository.saveAndFlush(bookingQuoteItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookingQuoteItem
        restBookingQuoteItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookingQuoteItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookingQuoteItemRepository.count();
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

    protected BookingQuoteItem getPersistedBookingQuoteItem(BookingQuoteItem bookingQuoteItem) {
        return bookingQuoteItemRepository.findById(bookingQuoteItem.getId()).orElseThrow();
    }

    protected void assertPersistedBookingQuoteItemToMatchAllProperties(BookingQuoteItem expectedBookingQuoteItem) {
        assertBookingQuoteItemAllPropertiesEquals(expectedBookingQuoteItem, getPersistedBookingQuoteItem(expectedBookingQuoteItem));
    }

    protected void assertPersistedBookingQuoteItemToMatchUpdatableProperties(BookingQuoteItem expectedBookingQuoteItem) {
        assertBookingQuoteItemAllUpdatablePropertiesEquals(
            expectedBookingQuoteItem,
            getPersistedBookingQuoteItem(expectedBookingQuoteItem)
        );
    }
}

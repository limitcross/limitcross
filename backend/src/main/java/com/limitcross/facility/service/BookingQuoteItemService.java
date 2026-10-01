package com.limitcross.facility.service;

import com.limitcross.facility.domain.BookingQuoteItem;
import com.limitcross.facility.repository.BookingQuoteItemRepository;
import com.limitcross.facility.service.dto.BookingQuoteItemDTO;
import com.limitcross.facility.service.mapper.BookingQuoteItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.BookingQuoteItem}.
 */
@Service
@Transactional
public class BookingQuoteItemService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingQuoteItemService.class);

    private final BookingQuoteItemRepository bookingQuoteItemRepository;

    private final BookingQuoteItemMapper bookingQuoteItemMapper;

    public BookingQuoteItemService(BookingQuoteItemRepository bookingQuoteItemRepository, BookingQuoteItemMapper bookingQuoteItemMapper) {
        this.bookingQuoteItemRepository = bookingQuoteItemRepository;
        this.bookingQuoteItemMapper = bookingQuoteItemMapper;
    }

    /**
     * Save a bookingQuoteItem.
     *
     * @param bookingQuoteItemDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingQuoteItemDTO save(BookingQuoteItemDTO bookingQuoteItemDTO) {
        LOG.debug("Request to save BookingQuoteItem : {}", bookingQuoteItemDTO);
        BookingQuoteItem bookingQuoteItem = bookingQuoteItemMapper.toEntity(bookingQuoteItemDTO);
        bookingQuoteItem = bookingQuoteItemRepository.save(bookingQuoteItem);
        return bookingQuoteItemMapper.toDto(bookingQuoteItem);
    }

    /**
     * Update a bookingQuoteItem.
     *
     * @param bookingQuoteItemDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingQuoteItemDTO update(BookingQuoteItemDTO bookingQuoteItemDTO) {
        LOG.debug("Request to update BookingQuoteItem : {}", bookingQuoteItemDTO);
        BookingQuoteItem bookingQuoteItem = bookingQuoteItemMapper.toEntity(bookingQuoteItemDTO);
        bookingQuoteItem = bookingQuoteItemRepository.save(bookingQuoteItem);
        return bookingQuoteItemMapper.toDto(bookingQuoteItem);
    }

    /**
     * Partially update a bookingQuoteItem.
     *
     * @param bookingQuoteItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingQuoteItemDTO> partialUpdate(BookingQuoteItemDTO bookingQuoteItemDTO) {
        LOG.debug("Request to partially update BookingQuoteItem : {}", bookingQuoteItemDTO);

        return bookingQuoteItemRepository
            .findById(bookingQuoteItemDTO.getId())
            .map(existingBookingQuoteItem -> {
                bookingQuoteItemMapper.partialUpdate(existingBookingQuoteItem, bookingQuoteItemDTO);

                return existingBookingQuoteItem;
            })
            .map(bookingQuoteItemRepository::save)
            .map(bookingQuoteItemMapper::toDto);
    }

    /**
     * Get all the bookingQuoteItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingQuoteItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookingQuoteItems");
        return bookingQuoteItemRepository.findAll(pageable).map(bookingQuoteItemMapper::toDto);
    }

    /**
     * Get one bookingQuoteItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingQuoteItemDTO> findOne(Long id) {
        LOG.debug("Request to get BookingQuoteItem : {}", id);
        return bookingQuoteItemRepository.findById(id).map(bookingQuoteItemMapper::toDto);
    }

    /**
     * Delete the bookingQuoteItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingQuoteItem : {}", id);
        bookingQuoteItemRepository.deleteById(id);
    }
}

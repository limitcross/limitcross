package com.limitcross.facility.service;

import com.limitcross.facility.domain.BookingAssignment;
import com.limitcross.facility.repository.BookingAssignmentRepository;
import com.limitcross.facility.service.dto.BookingAssignmentDTO;
import com.limitcross.facility.service.mapper.BookingAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.BookingAssignment}.
 */
@Service
@Transactional
public class BookingAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingAssignmentService.class);

    private final BookingAssignmentRepository bookingAssignmentRepository;

    private final BookingAssignmentMapper bookingAssignmentMapper;

    public BookingAssignmentService(
        BookingAssignmentRepository bookingAssignmentRepository,
        BookingAssignmentMapper bookingAssignmentMapper
    ) {
        this.bookingAssignmentRepository = bookingAssignmentRepository;
        this.bookingAssignmentMapper = bookingAssignmentMapper;
    }

    /**
     * Save a bookingAssignment.
     *
     * @param bookingAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingAssignmentDTO save(BookingAssignmentDTO bookingAssignmentDTO) {
        LOG.debug("Request to save BookingAssignment : {}", bookingAssignmentDTO);
        BookingAssignment bookingAssignment = bookingAssignmentMapper.toEntity(bookingAssignmentDTO);
        bookingAssignment = bookingAssignmentRepository.save(bookingAssignment);
        return bookingAssignmentMapper.toDto(bookingAssignment);
    }

    /**
     * Update a bookingAssignment.
     *
     * @param bookingAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingAssignmentDTO update(BookingAssignmentDTO bookingAssignmentDTO) {
        LOG.debug("Request to update BookingAssignment : {}", bookingAssignmentDTO);
        BookingAssignment bookingAssignment = bookingAssignmentMapper.toEntity(bookingAssignmentDTO);
        bookingAssignment = bookingAssignmentRepository.save(bookingAssignment);
        return bookingAssignmentMapper.toDto(bookingAssignment);
    }

    /**
     * Partially update a bookingAssignment.
     *
     * @param bookingAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingAssignmentDTO> partialUpdate(BookingAssignmentDTO bookingAssignmentDTO) {
        LOG.debug("Request to partially update BookingAssignment : {}", bookingAssignmentDTO);

        return bookingAssignmentRepository
            .findById(bookingAssignmentDTO.getId())
            .map(existingBookingAssignment -> {
                bookingAssignmentMapper.partialUpdate(existingBookingAssignment, bookingAssignmentDTO);

                return existingBookingAssignment;
            })
            .map(bookingAssignmentRepository::save)
            .map(bookingAssignmentMapper::toDto);
    }

    /**
     * Get all the bookingAssignments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingAssignmentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BookingAssignments");
        return bookingAssignmentRepository.findAll(pageable).map(bookingAssignmentMapper::toDto);
    }

    /**
     * Get all the bookingAssignments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BookingAssignmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookingAssignmentRepository.findAllWithEagerRelationships(pageable).map(bookingAssignmentMapper::toDto);
    }

    /**
     * Get one bookingAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingAssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get BookingAssignment : {}", id);
        return bookingAssignmentRepository.findOneWithEagerRelationships(id).map(bookingAssignmentMapper::toDto);
    }

    /**
     * Delete the bookingAssignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingAssignment : {}", id);
        bookingAssignmentRepository.deleteById(id);
    }
}

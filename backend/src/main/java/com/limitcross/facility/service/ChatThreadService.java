package com.limitcross.facility.service;

import com.limitcross.facility.domain.ChatThread;
import com.limitcross.facility.repository.ChatThreadRepository;
import com.limitcross.facility.service.dto.ChatThreadDTO;
import com.limitcross.facility.service.mapper.ChatThreadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.limitcross.facility.domain.ChatThread}.
 */
@Service
@Transactional
public class ChatThreadService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatThreadService.class);

    private final ChatThreadRepository chatThreadRepository;

    private final ChatThreadMapper chatThreadMapper;

    public ChatThreadService(ChatThreadRepository chatThreadRepository, ChatThreadMapper chatThreadMapper) {
        this.chatThreadRepository = chatThreadRepository;
        this.chatThreadMapper = chatThreadMapper;
    }

    /**
     * Save a chatThread.
     *
     * @param chatThreadDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatThreadDTO save(ChatThreadDTO chatThreadDTO) {
        LOG.debug("Request to save ChatThread : {}", chatThreadDTO);
        ChatThread chatThread = chatThreadMapper.toEntity(chatThreadDTO);
        chatThread = chatThreadRepository.save(chatThread);
        return chatThreadMapper.toDto(chatThread);
    }

    /**
     * Update a chatThread.
     *
     * @param chatThreadDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatThreadDTO update(ChatThreadDTO chatThreadDTO) {
        LOG.debug("Request to update ChatThread : {}", chatThreadDTO);
        ChatThread chatThread = chatThreadMapper.toEntity(chatThreadDTO);
        chatThread = chatThreadRepository.save(chatThread);
        return chatThreadMapper.toDto(chatThread);
    }

    /**
     * Partially update a chatThread.
     *
     * @param chatThreadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatThreadDTO> partialUpdate(ChatThreadDTO chatThreadDTO) {
        LOG.debug("Request to partially update ChatThread : {}", chatThreadDTO);

        return chatThreadRepository
            .findById(chatThreadDTO.getId())
            .map(existingChatThread -> {
                chatThreadMapper.partialUpdate(existingChatThread, chatThreadDTO);

                return existingChatThread;
            })
            .map(chatThreadRepository::save)
            .map(chatThreadMapper::toDto);
    }

    /**
     * Get all the chatThreads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatThreadDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ChatThreads");
        return chatThreadRepository.findAll(pageable).map(chatThreadMapper::toDto);
    }

    /**
     * Get all the chatThreads with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ChatThreadDTO> findAllWithEagerRelationships(Pageable pageable) {
        return chatThreadRepository.findAllWithEagerRelationships(pageable).map(chatThreadMapper::toDto);
    }

    /**
     * Get one chatThread by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatThreadDTO> findOne(Long id) {
        LOG.debug("Request to get ChatThread : {}", id);
        return chatThreadRepository.findOneWithEagerRelationships(id).map(chatThreadMapper::toDto);
    }

    /**
     * Delete the chatThread by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ChatThread : {}", id);
        chatThreadRepository.deleteById(id);
    }
}

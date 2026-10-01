package com.limitcross.facility.web.rest;

import com.limitcross.facility.repository.ChatThreadRepository;
import com.limitcross.facility.service.ChatThreadService;
import com.limitcross.facility.service.dto.ChatThreadDTO;
import com.limitcross.facility.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.limitcross.facility.domain.ChatThread}.
 */
@RestController
@RequestMapping("/api/chat-threads")
public class ChatThreadResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChatThreadResource.class);

    private static final String ENTITY_NAME = "chatThread";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatThreadService chatThreadService;

    private final ChatThreadRepository chatThreadRepository;

    public ChatThreadResource(ChatThreadService chatThreadService, ChatThreadRepository chatThreadRepository) {
        this.chatThreadService = chatThreadService;
        this.chatThreadRepository = chatThreadRepository;
    }

    /**
     * {@code POST  /chat-threads} : Create a new chatThread.
     *
     * @param chatThreadDTO the chatThreadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatThreadDTO, or with status {@code 400 (Bad Request)} if the chatThread has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChatThreadDTO> createChatThread(@Valid @RequestBody ChatThreadDTO chatThreadDTO) throws URISyntaxException {
        LOG.debug("REST request to save ChatThread : {}", chatThreadDTO);
        if (chatThreadDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatThread cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chatThreadDTO = chatThreadService.save(chatThreadDTO);
        return ResponseEntity.created(new URI("/api/chat-threads/" + chatThreadDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, chatThreadDTO.getId().toString()))
            .body(chatThreadDTO);
    }

    /**
     * {@code PUT  /chat-threads/:id} : Updates an existing chatThread.
     *
     * @param id the id of the chatThreadDTO to save.
     * @param chatThreadDTO the chatThreadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatThreadDTO,
     * or with status {@code 400 (Bad Request)} if the chatThreadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatThreadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChatThreadDTO> updateChatThread(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChatThreadDTO chatThreadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChatThread : {}, {}", id, chatThreadDTO);
        if (chatThreadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatThreadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatThreadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chatThreadDTO = chatThreadService.update(chatThreadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, chatThreadDTO.getId().toString()))
            .body(chatThreadDTO);
    }

    /**
     * {@code PATCH  /chat-threads/:id} : Partial updates given fields of an existing chatThread, field will ignore if it is null
     *
     * @param id the id of the chatThreadDTO to save.
     * @param chatThreadDTO the chatThreadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatThreadDTO,
     * or with status {@code 400 (Bad Request)} if the chatThreadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatThreadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatThreadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChatThreadDTO> partialUpdateChatThread(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChatThreadDTO chatThreadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChatThread partially : {}, {}", id, chatThreadDTO);
        if (chatThreadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatThreadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatThreadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChatThreadDTO> result = chatThreadService.partialUpdate(chatThreadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, chatThreadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chat-threads} : get all the chatThreads.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatThreads in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChatThreadDTO>> getAllChatThreads(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ChatThreads");
        Page<ChatThreadDTO> page;
        if (eagerload) {
            page = chatThreadService.findAllWithEagerRelationships(pageable);
        } else {
            page = chatThreadService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chat-threads/:id} : get the "id" chatThread.
     *
     * @param id the id of the chatThreadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatThreadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatThreadDTO> getChatThread(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ChatThread : {}", id);
        Optional<ChatThreadDTO> chatThreadDTO = chatThreadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatThreadDTO);
    }

    /**
     * {@code DELETE  /chat-threads/:id} : delete the "id" chatThread.
     *
     * @param id the id of the chatThreadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatThread(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ChatThread : {}", id);
        chatThreadService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

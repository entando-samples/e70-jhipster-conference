package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Conference;
import com.mycompany.myapp.repository.ConferenceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conference}.
 */
@Service
@Transactional
public class ConferenceService {

    private final Logger log = LoggerFactory.getLogger(ConferenceService.class);

    private final ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    /**
     * Save a conference.
     *
     * @param conference the entity to save.
     * @return the persisted entity.
     */
    public Conference save(Conference conference) {
        log.debug("Request to save Conference : {}", conference);
        return conferenceRepository.save(conference);
    }

    /**
     * Partially update a conference.
     *
     * @param conference the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Conference> partialUpdate(Conference conference) {
        log.debug("Request to partially update Conference : {}", conference);

        return conferenceRepository
            .findById(conference.getId())
            .map(existingConference -> {
                if (conference.getName() != null) {
                    existingConference.setName(conference.getName());
                }
                if (conference.getLocation() != null) {
                    existingConference.setLocation(conference.getLocation());
                }

                return existingConference;
            })
            .map(conferenceRepository::save);
    }

    /**
     * Get all the conferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Conference> findAll(Pageable pageable) {
        log.debug("Request to get all Conferences");
        return conferenceRepository.findAll(pageable);
    }

    /**
     * Get one conference by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Conference> findOne(Long id) {
        log.debug("Request to get Conference : {}", id);
        return conferenceRepository.findById(id);
    }

    /**
     * Delete the conference by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Conference : {}", id);
        conferenceRepository.deleteById(id);
    }
}

package com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

import java.util.List;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.exceptions.NotUniqueEntityException;
import com.foxminded.university.model.Audience;

@Service
public class AudienceService {

    private static final Logger log = LoggerFactory.getLogger(AudienceService.class);
    private AudienceDao audienceDao;

    public AudienceService(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    public void create(Audience audience) {
        log.debug("Checking audience with number - '{}' for uniqueness before creating", audience.getNumber());
        verifyUniqueness(audience);
        audienceDao.create(audience);
    }

    public Audience getById(int id) {
        return audienceDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Audience with id '%d' not exist", id)));
    }
    
    public List<Audience> getAll(){
        return audienceDao.getAll();
    }

    public Page<Audience> getAll(Pageable pageable) {
        return audienceDao.getAll(pageable);
    }

    public void update(Audience audience) {
        log.debug("Checking audience with id - '{}' for uniqueness before updating", audience.getId());
        verifyUniqueness(audience);
        audienceDao.update(audience);
    }

    public void delete(Audience audience) {
        audienceDao.delete(audience);
    }

    private void verifyUniqueness(Audience audience) {
        if (audienceDao.getByNumber(audience.getNumber())
                .filter(currentAudience -> currentAudience.getId() != audience.getId()).isPresent()) {
            throw new NotUniqueEntityException(format("Number = '%d' already exist", audience.getNumber()));
        }
    }
}

package com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

import java.util.List;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.exceptions.NotUniqueEntityException;
import com.foxminded.university.model.Holiday;

@Service
public class HolidayService {

    private static final Logger log = LoggerFactory.getLogger(HolidayService.class);
    private HolidayDao holidayDao;

    public HolidayService(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    public void create(Holiday holiday) {
        log.debug("Checking holiday with name - '{}' for uniqueness before creating", holiday.getName());
        virifyUniqueness(holiday);
        holidayDao.create(holiday);
    }

    public Holiday getById(int id) {
        log.debug("Retirieving holiday by id - '{}'", id);
        return holidayDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Can't retrieve holiday by id - '%d'", id)));
    }
    
    public List<Holiday> getAll() {
        return holidayDao.getAll();
    }
    
    public Page<Holiday> getAll(Pageable page) {
        return holidayDao.getAll(page);
    }

    public void update(Holiday holiday) {
        log.debug("Checking holiday with id - '{}' for uniqueness before updating", holiday.getId());
        virifyUniqueness(holiday);
        holidayDao.update(holiday);
    }

    public void delete(Holiday holiday) {
        holidayDao.delete(holiday);
    }

    private void virifyUniqueness(Holiday holiday) {
        if (holidayDao.getByDate(holiday.getDate())
                .filter(currentHoliday -> currentHoliday.getId() != holiday.getId())
                .isPresent()) {
            throw new NotUniqueEntityException(format("Name = '%s' already exist", holiday.getName()));
        }
    }
}

package com.foxminded.university.service;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.exceptions.InvalidLectureTimeException;
import com.foxminded.university.model.LectureTime;

@Service
public class LectureTimeService {

    private static final Logger log = LoggerFactory.getLogger(LectureTimeService.class);
    private LectureTimeDao lectureTimeDao;
    private UniversityConfigProperties properties;

    public LectureTimeService(LectureTimeDao lectureTimeDao, UniversityConfigProperties properties) {
        this.lectureTimeDao = lectureTimeDao;
        this.properties = properties;
    }

    public void create(LectureTime lectureTime) {
        log.debug("Checking lecture time - '{} - {}' for correctness before creating", lectureTime.getStartTime(),
                lectureTime.getEndTime());
        validateLectureTime(lectureTime);
        lectureTimeDao.create(lectureTime);
    }

    public LectureTime getById(int id) {
        log.debug("Retrieving lecture time by id - '{}'", id);
        return lectureTimeDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Can't retrieve lecture time by id - '%d'", id)));
    }

    public List<LectureTime> getAll() {
        return lectureTimeDao.getAll();
    }
    
    public Page<LectureTime> getAll(Pageable page) {
        return lectureTimeDao.getAll(page);
    }

    public void update(LectureTime lectureTime) {
        log.debug("Checking lecture time - '{} - {}' for correctness before updating", lectureTime.getStartTime(),
                lectureTime.getEndTime());
        validateLectureTime(lectureTime);
        lectureTimeDao.update(lectureTime);
    }

    public void delete(LectureTime lectureTime) {
        log.debug("Deleting lecture time with id - '{}'", lectureTime.getId());
        lectureTimeDao.delete(lectureTime);
    }

    private void validateLectureTime(LectureTime lectureTime) {
        if (Math.abs(Duration.between(lectureTime.getStartTime(), lectureTime.getEndTime())
                .toMinutes()) < properties.getMinLectureDurationInMinutes()
                || lectureTime.getEndTime().isBefore(lectureTime.getStartTime())) {
            throw new InvalidLectureTimeException(
                    format("time - '%s - %S' not valid", lectureTime.getStartTime(), lectureTime.getEndTime()));
        }
    }
}

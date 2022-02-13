package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.model.LectureTime;

@Repository
@Transactional
public class HibernateLectureTimeDao implements LectureTimeDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateLectureTimeDao.class);

    private SessionFactory sessionFactory;

    public HibernateLectureTimeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(LectureTime lectureTime) {
        log.debug("Creating lecture time {} - {}", lectureTime.getStartTime(), lectureTime.getEndTime());
        sessionFactory.getCurrentSession().save(lectureTime);
    }

    @Override
    public Optional<LectureTime> getById(int id) {
        log.debug("Retrieving lecture time with id - {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(LectureTime.class, id));
    }

    @Override
    public List<LectureTime> getAll() {
        log.debug("Retrieving all lectures times");
        return sessionFactory.getCurrentSession().createNamedQuery("LectureTime.getAll", LectureTime.class).getResultList();
    }

    @Override
    public Page<LectureTime> getAll(Pageable page) {
        log.debug("Retrieving lectures times by pages");
        List<LectureTime> lecturesTimes = sessionFactory.getCurrentSession().createNamedQuery("LectureTime.getAllPagable", LectureTime.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize()).getResultList();
        return new PageImpl<>(lecturesTimes, page, count());
    }

    @Override
    public void update(LectureTime lectureTime) {
        log.debug("Updating lecture time with id - {}", lectureTime.getId());
        sessionFactory.getCurrentSession().update(lectureTime);
    }

    @Override
    public void delete(LectureTime lectureTime) {
        log.debug("Deleting lecture time with id - {}", lectureTime.getId());
        sessionFactory.getCurrentSession().delete(lectureTime);
    }

    @Override
    public int count() {
        log.debug("Retrieving recordings count");
        return sessionFactory.getCurrentSession().createNamedQuery("LectureTime.getRecordingsCount", Long.class).uniqueResult()
                .intValue();
    }
}

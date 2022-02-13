package com.foxminded.university.dao.hibernate;

import java.time.LocalDate;
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

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.model.Holiday;

@Repository
@Transactional
public class HibernateHolidayDao implements HolidayDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateHolidayDao.class);

    private SessionFactory sessionFactory;

    public HibernateHolidayDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Holiday holiday) {
        log.debug("Creating holiday wiht name - {}", holiday.getName());
        sessionFactory.getCurrentSession().save(holiday);
    }

    @Override
    public Optional<Holiday> getById(int id) {
        log.debug("Retrieving holiday with id - {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Holiday.class, id));
    }

    @Override
    public List<Holiday> getAll() {
        log.debug("Retrieving all holidays");
        return sessionFactory.getCurrentSession().createNamedQuery("Holiday.getAll", Holiday.class).getResultList();
    }

    @Override
    public Page<Holiday> getAll(Pageable page) {
        log.debug("Retrieving holidays by pages");
        List<Holiday> holidays = sessionFactory.getCurrentSession().createNamedQuery("Holiday.getAllPagable", Holiday.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<>(holidays, page, count());
    }

    @Override
    public void update(Holiday holiday) {
        log.debug("Updating holiday with id - {}", holiday.getId());
        sessionFactory.getCurrentSession().update(holiday);
    }

    @Override
    public void delete(Holiday holiday) {
        log.debug("Deleting holiday with id - {}", holiday.getId());
        sessionFactory.getCurrentSession().delete(holiday);
    }

    @Override
    public int count() {
        log.debug("Retrieving recordings count");
        return sessionFactory.getCurrentSession().createNamedQuery("Holiday.getRecordingsCount", Long.class).uniqueResult()
                .intValue();
    }

    @Override
    public Optional<Holiday> getByDate(LocalDate date) {
        log.debug("Retrieving holiday with date - {}", date);
        return sessionFactory.getCurrentSession().createNamedQuery("Holiday.getByDate", Holiday.class).setParameter("date", date)
                .uniqueResultOptional();
    }
}

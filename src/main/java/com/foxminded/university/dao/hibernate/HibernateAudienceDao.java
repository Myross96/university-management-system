package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.model.Audience;

@Transactional
@Repository
public class HibernateAudienceDao implements AudienceDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateAudienceDao.class);

    private SessionFactory sessionFactory;

    public HibernateAudienceDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Audience audience) {
        log.debug("Creating audience with number {}", audience.getNumber());
        sessionFactory.getCurrentSession().save(audience);
    }

    @Override
    public Optional<Audience> getById(int id) {
        log.debug("Getting audience by id {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Audience.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Audience> getAll() {
        log.debug("Retrieving all audiences");
        return sessionFactory.getCurrentSession().createNamedQuery("Audience.getAll", Audience.class).getResultList();
    }

    @Override
    public Page<Audience> getAll(Pageable page) {
        List<Audience> audiences = sessionFactory.getCurrentSession().createNamedQuery("Audience.getAllPagable", Audience.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<>(audiences, page, count());
    }

    @Override
    public void update(Audience audience) {
        log.debug("Updating audience with id {} ", audience.getId());
        sessionFactory.getCurrentSession().update(audience);
    }

    @Override
    public void delete(Audience audience) {
        log.debug("Deleting audinece with id {}", audience.getId());
        sessionFactory.getCurrentSession().delete(audience);

    }

    @Override
    public int count() {
        log.debug("Retrieving recordings count");
        return sessionFactory.getCurrentSession().createNamedQuery("Audience.getRecordingsCount", Long.class).uniqueResult()
                .intValue();
    }

    @Override
    public Optional<Audience> getByNumber(int number) {
        log.debug("Retrievind audience by number {}", number);
        return sessionFactory.getCurrentSession().createNamedQuery("Audience.getByNamber", Audience.class)
                .setParameter("number", number).uniqueResultOptional();
    }
}

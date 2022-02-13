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

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.model.Vacation;

@Repository
@Transactional
public class HibernateVacationDao implements VacationDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateVacationDao.class);
    
    private SessionFactory sessionFactory;
    
    public HibernateVacationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Vacation vacation) {
        log.debug("Creating vacation from {} to {}", vacation.getStart(), vacation.getEnd());
        sessionFactory.getCurrentSession().save(vacation);
    }

    @Override
    public Optional<Vacation> getById(int id) {
        log.debug("Retrieving vacation with id - {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Vacation.class, id));
    }

    @Override
    public List<Vacation> getAll() {
        log.debug("Retrieving all vacations");
        return sessionFactory.getCurrentSession().createNamedQuery("Vacation.getAll", Vacation.class).getResultList();
    }

    @Override
    public Page<Vacation> getAll(Pageable page) {
        log.debug("Retrieving vacations by paged");
        List<Vacation> vacations = sessionFactory.getCurrentSession().createNamedQuery("Vacation.getAllPagable", Vacation.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<>(vacations, page, count());
    }

    @Override
    public void update(Vacation vacation) {
        log.debug("Updating vacations vith id - {}", vacation.getId());
        sessionFactory.getCurrentSession().update(vacation);        
    }

    @Override
    public void delete(Vacation vacation) {
        log.debug("Deleting vacation with id - {}", vacation.getId());
        sessionFactory.getCurrentSession().delete(vacation);        
    }

    @Override
    public int count() {
        log.debug("Retrieving recordings count");
        return sessionFactory.getCurrentSession().createNamedQuery("Vacation.getRecordingsCount", Long.class).getSingleResult()
                .intValue();
    }

    @Override
    public List<Vacation> getByTeacherId(int teacherId) {
        log.debug("Retrieving vacation by teacher id - {}", teacherId);
        return sessionFactory.getCurrentSession().createNamedQuery("Vacation.getByTeacherId", Vacation.class)
                .setParameter("id", teacherId).getResultList();
    }
}

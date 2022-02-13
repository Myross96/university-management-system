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

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;

@Repository
@Transactional
public class HibernateGroupDao implements GroupDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateGroupDao.class);

    private SessionFactory sessionFactory;

    public HibernateGroupDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Group group) {
        log.debug("Creating group with name - {}", group.getName());
        sessionFactory.getCurrentSession().save(group);
    }

    @Override
    public Optional<Group> getById(int id) {
        log.debug("Retrieving group with id - {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Group.class, id));
    }

    @Override
    public List<Group> getAll() {
        log.debug("Retrieving all groups");
        return sessionFactory.getCurrentSession().createNamedQuery("Group.getAll", Group.class).getResultList();
    }

    @Override
    public Page<Group> getAll(Pageable page) {
        log.debug("Retrieving group by pages");
        List<Group> groups = sessionFactory.getCurrentSession().createNamedQuery("Group.getAllPagable", Group.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<>(groups);
    }

    @Override
    public void update(Group group) {
        log.debug("Updating group with id - {}", group.getId());
        sessionFactory.getCurrentSession().update(group);
    }

    @Override
    public void delete(Group group) {
        log.debug("Deleting group wiht id - {}", group.getId());
        sessionFactory.getCurrentSession().delete(group);
    }

    @Override
    public int count() {
        return sessionFactory.getCurrentSession().createNamedQuery("Group.getRecordingsCount", Long.class).uniqueResult()
                .intValue();
    }

    @Override
    public Optional<Group> getByName(String name) {
        log.debug("Retrieving all groups with name - {}", name);
        return sessionFactory.getCurrentSession().createNamedQuery("Group.getByName", Group.class).setParameter("name", name)
                .uniqueResultOptional();
    }
}

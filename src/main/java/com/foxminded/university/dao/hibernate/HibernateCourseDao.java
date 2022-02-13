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

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;

@Repository
@Transactional
public class HibernateCourseDao implements CourseDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateCourseDao.class);

    private SessionFactory sessionFactory;

    public HibernateCourseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Course course) {
        log.debug("Creating course with name - {}", course.getName());
        sessionFactory.getCurrentSession().save(course);
    }

    @Override
    public Optional<Course> getById(int id) {
        log.debug("Retieve course by id - {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Course.class, id));
    }

    @Override
    public List<Course> getAll() {
        log.debug("Retriaving all cources");
        return sessionFactory.getCurrentSession().createNamedQuery("Course.getAll", Course.class).getResultList();
    }

    @Override
    public Page<Course> getAll(Pageable page) {
        List<Course> courses = sessionFactory.getCurrentSession().createNamedQuery("Course.getAllPagable", Course.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize()).getResultList();
        return new PageImpl<>(courses, page, count());
    }

    @Override
    public void update(Course course) {
        log.debug("Updating course wiht id - {}", course.getId());
        sessionFactory.getCurrentSession().update(course);
    }

    @Override
    public void delete(Course course) {
        log.debug("Deleting course with id - {}", course.getId());
        sessionFactory.getCurrentSession().delete(course);
    }

    @Override
    public int count() {
        log.debug("Retrieving courses recordings count");
        return sessionFactory.getCurrentSession().createNamedQuery("Course.getRecordingsCount", Long.class).uniqueResult()
                .intValue();
    }

    @Override
    public Optional<Course> getByName(String name) {
        return sessionFactory.getCurrentSession().createNamedQuery("Course.getByName", Course.class).setParameter("name", name)
                .uniqueResultOptional();
    }
}

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

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

@Repository
@Transactional
public class HibernateTeacherDao implements TeacherDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateTeacherDao.class);

    private SessionFactory sessionFactory;

    public HibernateTeacherDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Teacher teacher) {
        log.debug("Creating teacher with name {} {}", teacher.getFirstName(), teacher.getLastName());
        sessionFactory.getCurrentSession().save(teacher);
    }

    @Override
    public Optional<Teacher> getById(int id) {
        log.debug("Retrieving teacher with id - {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Teacher.class, id));
    }

    @Override
    public List<Teacher> getAll() {
        log.debug("Retrieving all teachers");
        return sessionFactory.getCurrentSession().createNamedQuery("Teacher.getAll", Teacher.class).getResultList();
    }

    @Override
    public Page<Teacher> getAll(Pageable page) {
        log.debug("Get teachers by pages");
        List<Teacher> teachers = sessionFactory.getCurrentSession().createNamedQuery("Teacher.getAllPagable", Teacher.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<>(teachers, page, count());
    }

    @Override
    public void update(Teacher teacher) {
        log.debug("Update tacher wiht id - {}", teacher.getId());
        sessionFactory.getCurrentSession().update(teacher);
    }

    @Override
    public void delete(Teacher teacher) {
        log.debug("Deleting teacher with id - {}", teacher.getId());
        sessionFactory.getCurrentSession().delete(teacher);
    }

    @Override
    public int count() {
        log.debug("Retrieving recordings count");
        return sessionFactory.getCurrentSession().createNamedQuery("Teacher.getRecordingsCount", Long.class).getSingleResult()
                .intValue();
    }

    @Override
    public List<Teacher> getByCourse(Course course) {
        log.debug("Retrieving all teachers where course id - {}", course.getId());
        return sessionFactory.getCurrentSession().createNamedQuery("Teacher.getByCourseId", Teacher.class)
                .setParameter("id", course.getId()).getResultList();
    }
}

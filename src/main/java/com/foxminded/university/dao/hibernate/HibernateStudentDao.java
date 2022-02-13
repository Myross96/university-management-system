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

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;

@Repository
@Transactional
public class HibernateStudentDao implements StudentDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateStudentDao.class);
    
    private SessionFactory sessionFactory;
    
    public HibernateStudentDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Student student) {
        log.debug("Creating student - {} {}", student.getFirstName(), student.getLastName());
        sessionFactory.getCurrentSession().save(student);
    }

    @Override
    public Optional<Student> getById(int id) {
        log.debug("Fetching student by id - {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Student.class, id));
    }

    @Override
    public List<Student> getAll() {
        log.debug("Retrieving all students");
        return sessionFactory.getCurrentSession().createNamedQuery("Student.getAll", Student.class).getResultList();
    }

    @Override
    public Page<Student> getAll(Pageable page) {
        log.debug("Retrieving students by pages");
        List<Student> students = sessionFactory.getCurrentSession().createNamedQuery("Student.getAllPagable", Student.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<>(students, page, count());
    }

    @Override
    public void update(Student student) {
        log.debug("Updating student with id - {}", student.getId());
        sessionFactory.getCurrentSession().update(student);
    }

    @Override
    public void delete(Student student) {
        log.debug("Deleting student with id - {}", student.getId());
        sessionFactory.getCurrentSession().delete(student);
    }

    @Override
    public int count() {
        log.debug("Retrieving recordings count");
        return sessionFactory.getCurrentSession().createNamedQuery("Student.getRecordingsCount", Long.class).uniqueResult().intValue();
    }

    @Override
    public List<Student> getByGroupId(int groupId) {
        log.debug("Retrieving students with group id - {}", groupId);
        return sessionFactory.getCurrentSession().createNamedQuery("Student.getAllByGroupID", Student.class)
                .setParameter("id", groupId).getResultList();
    }
}

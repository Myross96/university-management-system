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

import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.model.Lecture;

@Repository
@Transactional
public class HibernateLectureDao implements LectureDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateLectureDao.class);

    private SessionFactory sessionFactory;
    
    
    public HibernateLectureDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Lecture lecture) {
        log.debug("Creating lecture with name- {}", lecture.getName());
        sessionFactory.getCurrentSession().save(lecture);
    }

    @Override
    public Optional<Lecture> getById(int id) {
        log.debug("Retrieve lecture with id - {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Lecture.class, id));
    }

    @Override
    public List<Lecture> getAll() {
        log.debug("Retrieving all lectures");
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getAll", Lecture.class).getResultList();
    }

    @Override
    public Page<Lecture> getAll(Pageable page) {
        log.debug("Retrieving all lectures by pages");
        List<Lecture> lectures = sessionFactory.getCurrentSession().createNamedQuery("Lecture.getAllPagable", Lecture.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
        return new PageImpl<>(lectures, page, count());
    }

    @Override
    public void update(Lecture lecture) {
        log.debug("Updating lecture with id - {}", lecture.getId());
        sessionFactory.getCurrentSession().update(lecture);
    }

    @Override
    public void delete(Lecture lecture) {
        log.debug("Deleting lecture with id - {}", lecture.getId());
        sessionFactory.getCurrentSession().delete(lecture);
    }

    @Override
    public int count() {
        log.debug("Retrieving recordings count");
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getRecordingsCount", Long.class).getSingleResult()
                .intValue();
    }

    @Override
    public List<Lecture> getByTeacherAndDate(int teacherId, LocalDate date) {
        log.debug("Retrieving lectures where teacher id - {} and date - {}", teacherId, date);
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getByTeacherAndDate", Lecture.class)
                .setParameter("id", teacherId).setParameter("date", date).getResultList();
    }

    @Override
    public List<Lecture> getByTeacherAndDateRange(int teacherId, LocalDate startDate, LocalDate endDate) {
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getByTeacherAndMonth", Lecture.class)
                .setParameter("id", teacherId).setParameter("start", startDate).setParameter("end", endDate)
                .getResultList();
    }

    @Override
    public List<Lecture> getByStudentAndDate(int studentId, LocalDate date) {
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getByStudentAndDate", Lecture.class).setParameter("id", studentId)
                .setParameter("date", date).getResultList();
    }

    @Override
    public List<Lecture> getByStudentAndDateRange(int studentId, LocalDate startDate, LocalDate endDate) {
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getByStudentAndMonth", Lecture.class)
                .setParameter("id", studentId).setParameter("start", startDate).setParameter("end", endDate)
                .getResultList();
    }

    @Override
    public Optional<Lecture> getByGroupDateAndLectureTime(int groupId, LocalDate date, int lectureTimeId) {
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getByGroupDateAndLectureTime", Lecture.class)
                .setParameter("id", groupId)
                .setParameter("date", date)
                .setParameter("lectureTime", lectureTimeId)
                .uniqueResultOptional();
    }

    @Override
    public Optional<Lecture> getByAudienceDateAndLectureTime(int audienceId, LocalDate date, int lectureTimeId) {
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getByAudienceDateAndLectureTime", Lecture.class)
                .setParameter("id", audienceId)
                .setParameter("date", date)
                .setParameter("lectureTime", lectureTimeId)
                .uniqueResultOptional();
    }

    @Override
    public Optional<Lecture> getByTeacherDateAndLectureTime(int teacherId, LocalDate date, int lectureTimeId) {
        return sessionFactory.getCurrentSession().createNamedQuery("Lecture.getByTeacherDateAndLectureTime", Lecture.class)
                .setParameter("id", teacherId)
                .setParameter("date", date)
                .setParameter("lectureTime", lectureTimeId)
                .uniqueResultOptional();
    }
}

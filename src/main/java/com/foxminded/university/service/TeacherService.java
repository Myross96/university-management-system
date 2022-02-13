package com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

import java.util.List;
import java.util.stream.Collectors;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

@Service
public class TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);
    private TeacherDao teacherDao;

    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    public void create(Teacher teacher) {
        log.debug("Creating tracher - '{} {}'", teacher.getFirstName(), teacher.getLastName());
        teacherDao.create(teacher);
    }

    public Teacher getById(int id) {
        log.debug("Retrieving tracher by id - '{}'", id);
        return teacherDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Can't retrieve teacher by id - '%d'", id)));
    }

    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }
    
    public Page<Teacher> getAll(Pageable page) {
        log.debug("Listing teachers");
        return teacherDao.getAll(page);
    }
    
    public List<Teacher> getByCourses(List<Course> courses) {
        return courses.stream()
                .map(teacherDao::getByCourse)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public void update(Teacher teacher) {
        log.debug("Updating teacher with id - '{}'", teacher.getId());
        teacherDao.update(teacher);
    }

    public void delete(Teacher teacher) {
        log.debug("Deleting teacher wit id - '{}'", teacher.getId());
        teacherDao.delete(teacher);
    }
}

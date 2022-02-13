package com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

import java.util.List;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.exceptions.NotUniqueEntityException;
import com.foxminded.university.model.Course;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    private CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public void create(Course course) {
        log.debug("Checking course with name - '{}' for uniqueness before creating", course.getName());
        verifyUniqueness(course);
        courseDao.create(course);
    }

    public Course getById(int id) {
        log.debug("Retrieving course by id - '{}'", id);
        return courseDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Course with id - '%d' not exist", id)));
    }
    
    public List<Course> getAll() {
        return courseDao.getAll();
    }
    
    public Page<Course> getAll(Pageable page) {
        return courseDao.getAll(page);
    }

    public void update(Course course) {
        log.debug("Checking course with id - '{}' for uniqueness before updating", course.getId());
        verifyUniqueness(course);
        courseDao.update(course);
    }

    public void delete(Course course) {
        courseDao.delete(course);
    }

    private void verifyUniqueness(Course course) {
        if (courseDao.getByName(course.getName())
                .filter(currentCourse -> currentCourse.getId() != course.getId())
                .isPresent()) {
            throw new NotUniqueEntityException(format("Name = '%s' already exist", course.getName()));
        }
    }
}

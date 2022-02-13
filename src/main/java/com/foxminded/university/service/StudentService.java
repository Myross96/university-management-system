package com.foxminded.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

import java.util.List;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.exceptions.NoMorePlaceInGroupException;
import com.foxminded.university.model.Student;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private StudentDao studentDao;
    private GroupDao groupDao;
    private UniversityConfigProperties properties;

    public StudentService(StudentDao studentDao, GroupDao groupDao, UniversityConfigProperties properties) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.properties = properties;
    }

    public void create(Student student) {
        log.debug("checking place in gorup - {}", student.getGroup().getName());
        validateGroupPlace(student);
        studentDao.create(student);
    }

    public Student getById(int id) {
        log.debug("Retrieving student by id - '{}'", id);
        return studentDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Can't retrieve student by id - '%d'", id)));
    }

    public List<Student> getAll() {
        return studentDao.getAll();
    }
    
    public Page<Student> getAll(Pageable page) {
        log.debug("Retrieving all students");
        return studentDao.getAll(page);
    }

    public void update(Student student) {
        log.debug("Checking place in group - {}", student.getGroup().getName());
        validateGroupPlace(student);
        studentDao.update(student);
    }

    public void delete(Student student) {
        log.debug("Deleting student with id - '{}'", student.getId());
        studentDao.delete(student);
    }

    private void validateGroupPlace(Student student) {
        if (groupDao.getById(student.getGroup().getId())
                .filter(currentGroup -> currentGroup.getStudents().size() > properties.getMaxGroupSize()).isPresent()) {
            throw new NoMorePlaceInGroupException(format("Group - '%s' is already full", student.getGroup().getName()));
        }
    }
}

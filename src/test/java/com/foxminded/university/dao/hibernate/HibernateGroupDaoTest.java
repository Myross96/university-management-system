package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.TestConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import static com.foxminded.university.dao.hibernate.HibernateGroupDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateStudentDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateCourseDaoTest.TestData.*;

@SpringJUnitConfig(classes = TestConfig.class)
class HibernateGroupDaoTest {
    
    @Autowired
    private GroupDao groupDao;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenNewGroup_whenCreate_thenCreateNewGroup() {
        Group group = group1;
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        group.setCourses(courses);

        groupDao.create(group);

        Group actual = hibernateTemplate.get(Group.class, group.getId());
        assertEquals(group, actual);
    }

    @Test
    void givenGroupId_whenGetById_thenGetGroupById() {
        Group expected = group2;

        List<Student> students = new ArrayList<>();
        Student firstStudent = student2;
        firstStudent.setGroup(expected);
        students.add(firstStudent);

        Student secondStudent = student4;
        secondStudent.setGroup(expected);
        students.add(secondStudent);

        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        courses.add(course4);

        expected.setStudents(students);
        expected.setCourses(courses);

        Group actual = groupDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void givenGroupName_whenGetByName_thenGetGroupByName() {
        Group expected = group2;

        List<Student> students = new ArrayList<>();
        Student firstStudent = student2;
        firstStudent.setGroup(expected);
        students.add(firstStudent);

        Student secondStudent = student4;
        secondStudent.setGroup(expected);
        students.add(secondStudent);

        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        courses.add(course4);

        expected.setStudents(students);
        expected.setCourses(courses);

        Group actual = groupDao.getByName(expected.getName()).get();

        assertEquals(expected, actual);
    }
    
    @Test
    void GivenWrongName_whenGetByName_thenReturnEmptyOptional() {
        Optional<Group> expected = Optional.empty();
        
        Optional<Group> actual = groupDao.getByName("Wrong name");

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableGroups_whenGetAll_thenGetAll() {        
        List<Group> expected = hibernateTemplate.loadAll(Group.class);

        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableGroups_whenGetAll_thenGetAllByPages() {
        List<Group> content = hibernateTemplate.loadAll(Group.class);
        Pageable page = PageRequest.of(0, 20);
        Page<Group> expected = new PageImpl<>(content, page, 3);

        Page<Group> actual = groupDao.getAll(page);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenNotEnoughData_whenGetAll_thenReturnEmptyList() {
        List<Group> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<Group> expected = new PageImpl<Group>(content, page, 3);

        Page<Group> actual = groupDao.getAll(page);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableGroups_whenGetCountOfRecordings_thenGetCountOfRecordings() {
        int expected = 3;
        
        int actual = groupDao.count();
        
        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenGroup_whenUpdate_thenUpdateGroup() {
        List<Course> courses = new ArrayList<>();
        courses.add(course2);

        List<Student> students = new ArrayList<>();
        students.add(student2);

        Group expected = group5;
        expected.setCourses(courses);
        expected.setStudents(students);

        groupDao.update(expected);

        Group actual = hibernateTemplate.get(Group.class, expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    void givenGroup_whenDelete_thenThrowException() {
        Group group = group2;

        assertThrows(RuntimeException.class, () -> {
            groupDao.delete(group);
        });
    }

    interface TestData {

        Group group1 = new Group.Builder()
                .name("test group")
                .build();

        Group group2 = new Group.Builder()
                .id(1)
                .name("first group")
                .build();

        Group group3 = new Group.Builder()
                .id(2)
                .name("second group")
                .build();

        Group group4 = new Group.Builder()
                .id(3)
                .name("thirt group")
                .build();

        Group group5 = new Group.Builder()
                .id(1)
                .name("TestName")
                .build();
        
        Group group6 = new Group.Builder()
                .id(0)
                .name("Default")
                .build();
    }
}

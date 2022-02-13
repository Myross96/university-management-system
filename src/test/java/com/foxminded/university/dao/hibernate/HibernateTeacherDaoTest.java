package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Teacher;
import static com.foxminded.university.dao.hibernate.HibernateTeacherDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateGroupDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateCourseDaoTest.TestData.*;


@SpringJUnitConfig(classes = TestConfig.class)
class HibernateTeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenNewTeacher_whenCreate_thenCreateTeacher() {
        List<Course> courses = new ArrayList<>();
        courses.add(course2);

        List<Group> groups = new ArrayList<>();
        groups.add(group2);

        Teacher teacher = teacher1;
        teacher.setGroups(groups);
        teacher.setCourses(courses);

        teacherDao.create(teacher);

        Teacher actual = hibernateTemplate.get(Teacher.class, teacher.getId());
        assertEquals(teacher, actual);
    }

    @Test
    void givenTeacherId_whenGetById_thenGetTeacherById() {
        List<Course> courses = new ArrayList<>();
        courses.add(course4);

        List<Group> groups = new ArrayList<>();
        groups.add(group2);

        Teacher expected = teacher2;
        expected.setCourses(courses);
        expected.setGroups(groups);

        Teacher actual = teacherDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableTeachers_whenGetAll_thenGetAll() {
        List<Teacher> expected = hibernateTemplate.loadAll(Teacher.class);

        List<Teacher> actual = teacherDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableTeachers_whenGetAll_thenGetAllByPages() {
        List<Teacher> content = hibernateTemplate.loadAll(Teacher.class);
        Pageable page = PageRequest.of(0, 20);
        Page<Teacher> expected = new PageImpl<>(content, page, 3);

        Page<Teacher> actual = teacherDao.getAll(page);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenNotEnoughData_whenGetAll_thenReturnEmptyList() {
        List<Teacher> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<Teacher> expected = new PageImpl<>(content, page, 3);

        Page<Teacher> actual = teacherDao.getAll(page);

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenTeacher_whenUpdate_thenUpdateTeacher() {
        List<Course> courses = new ArrayList<>();
        Course course1 = course4;
        courses.add(course1);

        Course course2 = course5;
        courses.add(course2);

        List<Group> groups = new ArrayList<>();
        Group firstGroup = group2;
        groups.add(firstGroup);

        Group secondGroup = group3;
        groups.add(secondGroup);

        Teacher expected = teacher3;
        expected.setCourses(courses);
        expected.setGroups(groups);

        teacherDao.update(expected);

        Teacher actual = hibernateTemplate.get(Teacher.class, expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    void givenTeacher_whenDelete_thenThrowRuntimeException() {
        Teacher teacher = new Teacher();
        teacher.setId(1);

        assertThrows(RuntimeException.class, () -> {
            teacherDao.delete(teacher);
        });
    }
    
    @Test
    void givenTableTeachers_whenGetRecordingsCount_thenGetCountOfRecordings() {
        int expected = 3;
        
        int actual = teacherDao.count();
        
        assertEquals(expected, actual);
    }

    @Test
    void givenTableTeachers_whenGetByCourse_thenReturnTeachersList() {
        Course course = course2;
        int expected = 1;
        
        int actual = teacherDao.getByCourse(course).size();
        
        assertEquals(expected, actual);        
    }
    
    interface TestData {

        Teacher teacher1 = new Teacher.Builder()
                .firstName("Test")
                .lastName("Test")
                .birthDate(LocalDate.parse("1985-06-17"))
                .gender(Gender.MALE)
                .address("test address")
                .email("test@email.com")
                .postCode("523695")
                .academicDegree(Degree.DOCTOR)
                .experience(13)
                .build();

        Teacher teacher2 = new Teacher.Builder()
                .id(1)
                .firstName("Jennifer")
                .lastName("Davies")
                .birthDate(LocalDate.parse("1973-09-04"))
                .gender(Gender.FEMALE)
                .email("email1")
                .address("address1")
                .postCode("78542")
                .academicDegree(Degree.BACHELOR)
                .experience(12)
                .build();

        Teacher teacher3 = new Teacher.Builder()
                .id(1)
                .firstName("Test")
                .lastName("Davies")
                .birthDate(LocalDate.parse("1973-09-04"))
                .gender(Gender.FEMALE)
                .email("email1")
                .address("address1")
                .postCode("78542")
                .academicDegree(Degree.BACHELOR)
                .experience(12)
                .build();

        Teacher teacher4 = new Teacher.Builder()
                .id(2)
                .firstName("James")
                .lastName("Jones")
                .birthDate(LocalDate.parse("1981-06-15"))
                .gender(Gender.MALE)
                .email("email2")
                .address("address2")
                .postCode("42318")
                .academicDegree(Degree.MASTER)
                .experience(4)
                .build();
    }
}

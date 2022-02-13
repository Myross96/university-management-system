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
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import static com.foxminded.university.dao.hibernate.HibernateStudentDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateGroupDaoTest.TestData.*;

@SpringJUnitConfig(classes = TestConfig.class)
class HibernateStudentDaoTest {
    
    @Autowired
    private StudentDao studentDao;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenNewStudent_whenCreate_thenCreateNewStudent() {
        Group group = group2;
        Student student = student1;
        student.setGroup(group);


        studentDao.create(student);

        Student actual = hibernateTemplate.get(Student.class, student.getId());
        assertEquals(student, actual);
    }

    @Test
    void givenStudentId_whenGetById_thenGetStudentById() {
        Group group = group2;
        Student expected = TestData.student2;
        expected.setGroup(group);

        Student actual = studentDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableStudents_whenGetAll_thenGetAll() {
        List<Student> expected = hibernateTemplate.loadAll(Student.class);
        
        List<Student> actual = studentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableStudents_whenGetAll_thenGetAllByPages() {
        List<Student> content = hibernateTemplate.loadAll(Student.class);
        Pageable page = PageRequest.of(0, 20);
        Page<Student> expected = new PageImpl<>(content, page, 5);
        
        Page<Student> actual = studentDao.getAll(page);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenNotEnoughData_whenGetAll_thenReturnEmptyList() {
        List<Student> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<Student> expected = new PageImpl<>(content, page, 5);
        
        Page<Student> actual = studentDao.getAll(page);

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenStudent_whenUpdate_thenUpdateStudent() {
        Group group = group3;
        Student expected = student3;
        expected.setGroup(group);

        studentDao.update(expected);

        Student actual = hibernateTemplate.get(Student.class, expected.getId());        
        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenStudent_whenDelete_thenDeleteStudent() {
        Student studnet = student2;
        int expected = 4;

        studentDao.delete(studnet);

        int actual = studentDao.count();
        assertEquals(expected, actual);
    }

    @Test
    void givenGroupId_whenGetByGroupId_thenGetStudentByGroupId() {
        List<Student> expected = new ArrayList<>();
        Student firstStudent = student2;
        expected.add(firstStudent);
        Student secondStudent = student4;
        expected.add(secondStudent);
        int groupId = 1;

        List<Student> actual = studentDao.getByGroupId(groupId);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenStudentTable_whenGetRecordingsCount_thenReturnRecordingsCount() {
        int expected = 5;
        
        int actual = studentDao.count();
        
        assertEquals(expected, actual);
    }

    interface TestData {

        Student student1 = new Student.Builder()
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.parse("1997-09-13"))
                .gender(Gender.MALE)
                .email("testEmail")
                .address("test address")
                .postCode("54823")
                .build();

        Student student2 = new Student.Builder()
                .id(1).firstName("James")
                .lastName("Smith")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("1995-07-05"))
                .address("address1")
                .email("email1")
                .postCode("13256")
                .build();

        Student student3 = new Student.Builder()
                .id(1)
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.parse("1995-06-05"))
                .address("testAddress")
                .email("testEmail")
                .postCode("52175")
                .build();
        
        Student student4 = new Student.Builder()
                .id(4)
                .firstName("Barbara")
                .lastName("Davies")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.parse("1995-03-12"))
                .address("address4")
                .email("email4")
                .postCode("48236")
                .build();
    }
}

package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.exceptions.NoMorePlaceInGroupException;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;
import static com.foxminded.university.service.GroupServiceTest.TestData.*;
import static com.foxminded.university.service.StudentServiceTest.TestData.*;

@PropertySource("classpath:application.properties")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private UniversityConfigProperties properties;

    @InjectMocks
    private StudentService studentService;
    
    @BeforeEach
    private void initUniversityProperties() {
        lenient().when(properties.getMaxGroupSize()).thenReturn(1);
    }

    @Test
    void givenCorrectStudent_whenCreate_thenCreate() {
        List<Student> students = new ArrayList<>();
        student1.getGroup().setStudents(students);
        int id = student1.getGroup().getId();
        when(groupDao.getById(id)).thenReturn(Optional.ofNullable(student1.getGroup()));

        studentService.create(student1);

        verify(studentDao).create(student1);
    }

    @Test
    void givenStudentWithGroupWithoutPlaces_whenCreate_thenThrowException() {
        List<Student> students = new ArrayList<>();
        students.add(student3);
        students.add(student2);
        student1.getGroup().setStudents(students);
        int id = student1.getGroup().getId();
        when(groupDao.getById(id)).thenReturn(Optional.ofNullable(student1.getGroup()));
        
        Exception exception = assertThrows(NoMorePlaceInGroupException.class, () -> studentService.create(student1));

        assertEquals("Group - 'group2' is already full", exception.getMessage());
    }

    @Test
    void givenCorrectId_whenGetById_thenGetById() {
        Student expected = student2;
        int id = expected.getId();
        when(studentDao.getById(id)).thenReturn(Optional.ofNullable(expected));

        Student actual = studentService.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    void givenStudentsInStorage_whenGetAll_thenGetAll() {
        List<Student> expected = new ArrayList<>();
        expected.add(student2);
        when(studentDao.getAll()).thenReturn(expected);

        List<Student> actual = studentService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenStudentsInStorage_whenGetAll_thenGetAllByPages() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        Page<Student> expected = new PageImpl<>(students);
        when(studentDao.getAll(PageRequest.of(1, 1))).thenReturn(expected);

        assertEquals(expected, studentService.getAll(PageRequest.of(1, 1)));
    }

    @Test
    void givenCorrectStudent_whenUpdate_thenUpdate() {
        List<Student> students = new ArrayList<>();
        student2.getGroup().setStudents(students);
        int id = student2.getGroup().getId();
        when(groupDao.getById(id)).thenReturn(Optional.ofNullable(student2.getGroup()));

        studentService.update(student2);

        verify(studentDao).update(student2);
    }

    @Test
    void givenStudent_whenDelete_thenThrowException() {
        studentService.delete(student2);

        verify(studentDao).delete(student2);
    }

    interface TestData{
        Student student1 = new Student.Builder()
                .firstName("testFirstName")
                .lastName("testLastName")
                .birthDate(LocalDate.parse("1999-03-25"))
                .email("student1@email")
                .address("student 1 address")
                .gender(Gender.MALE)
                .postCode("54219")
                .group(group2)
                .build();
        
        Student student2 = new Student.Builder()
                .id(2)
                .firstName("student1FirstName")
                .lastName("student1LastName")
                .birthDate(LocalDate.parse("2000-03-25"))
                .email("student2@email")
                .address("student 2 address")
                .gender(Gender.FEMALE)
                .postCode("54638")
                .group(group2)
                .build();
        
        Student student3 = new Student.Builder()
                .id(3)
                .firstName("student2FirstName")
                .lastName("student2LastName")
                .birthDate(LocalDate.parse("2000-03-25"))
                .email("student2@email")
                .address("student 2 address")
                .gender(Gender.MALE)
                .postCode("54638")
                .group(group2)
                .build();
    }
}

package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Teacher;
import static com.foxminded.university.service.GroupServiceTest.TestData.group2;
import static com.foxminded.university.service.CourseServiceTest.TestData.course2;
import static com.foxminded.university.service.TeacherServiceTest.TestData.teacher1;
import static com.foxminded.university.service.TeacherServiceTest.TestData.teacher2;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;
    @Mock
    private CourseDao courseDao;
    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void givenTeacher_whenCreate_thenCreateNewTeacher() {
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        teacher1.setCourses(courses);
        teacher1.setGroups(groups);

        teacherService.create(teacher1);

        verify(teacherDao).create(teacher1);
    }

    @Test
    void givenCorrectId_whenGetById_thenGetById() {
        Teacher expected = teacher2;
        int id = expected.getId();
        when(teacherDao.getById(id)).thenReturn(Optional.ofNullable(expected));

        Teacher actual = teacherService.getById(id);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTeachersinStorage_whenGetAll_thenGetAll() {
        List<Teacher> expected = new ArrayList<>();
        expected.add(teacher2);
        when(teacherDao.getAll()).thenReturn(expected);

        List<Teacher> actual = teacherService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTeachersinStorage_whenGetAll_thenGetAllByPages() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher2);
        Page<Teacher> expected = new PageImpl<>(teachers);
        when(teacherDao.getAll(PageRequest.of(1, 1))).thenReturn(expected);

        assertEquals(expected, teacherService.getAll(PageRequest.of(1, 1)));
    }

    @Test
    void givenTeacher_whenDelete_thenDelete() {
        teacherService.delete(teacher2);

        verify(teacherDao).delete(teacher2);
    }
    
    interface TestData {
        Teacher teacher1 = new Teacher.Builder()
                .firstName("teacher1")
                .lastName("teacher1")
                .birthDate(LocalDate.parse("1983-05-17"))
                .address("teacher1 address")
                .email("teacher1@email")
                .experience(7)
                .gender(Gender.MALE)
                .postCode("75942")
                .academicDegree(Degree.MASTER)
                .build();
        
        Teacher teacher2 = new Teacher.Builder()
                .id(1)
                .firstName("teacher2")
                .lastName("teacher2")
                .birthDate(LocalDate.parse("1983-05-17"))
                .address("teacher2 address")
                .email("teacher2@email")
                .experience(5)
                .gender(Gender.MALE)
                .postCode("32642")
                .academicDegree(Degree.BACHELOR)
                .build();
    }
}

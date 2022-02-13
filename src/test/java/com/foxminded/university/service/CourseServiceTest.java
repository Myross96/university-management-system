package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.exceptions.NotUniqueEntityException;
import com.foxminded.university.model.Course;
import static com.foxminded.university.service.CourseServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private CourseService courseService;

    @Test
    void givenCourse_whenCreate_thenCreate() {
        when(courseDao.getByName(course1.getName())).thenReturn(Optional.empty());

        courseService.create(course1);

        verify(courseDao).create(course1);
    }

    @Test
    void givenAlreadyExistCourse_whenCreate_thenNotCreate() {
        when(courseDao.getByName(course1.getName())).thenReturn(Optional.of(course4));
        Exception exception = assertThrows(NotUniqueEntityException.class, () -> courseService.create(course1));

        assertEquals("Name = 'Test' already exist", exception.getMessage());
    }

    @Test
    void givenCorrectId_whenGetById_thenGetById() {
        Course expected = course2;
        int id = expected.getId();
        when(courseDao.getById(id)).thenReturn(Optional.ofNullable(expected));

        Course actual = courseService.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    void ginenCoursesInStorage_whenGetAll_thenGetAll() {
        List<Course> expected = new ArrayList<>();
        expected.add(course2);
        when(courseDao.getAll()).thenReturn(expected);

        List<Course> actual = courseService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenCourse_whenUpdate_thenUpdate() {
        when(courseDao.getByName(course2.getName())).thenReturn(Optional.of(course2));

        courseService.update(course2);

        verify(courseDao).update(course2);
    }

    @Test
    void givenCourse_whenDelete_thenDeleteCourse() {
        courseService.delete(course2);

        verify(courseDao).delete(course2);
    }

    interface TestData{
        Course course1 = new Course.Builder()
                .name("Test")
                .description("Test description")
                .build();
        
        Course course2 = new Course.Builder()
                .id(1)
                .name("course2")
                .description("course2 description")
                .build();
        
        Course course3 = new Course.Builder()
                .id(2)
                .name("course3")
                .description("course 3 description")
                .build();
        
        Course course4 = new Course.Builder()
                .id(3)
                .name("Test")
                .description("Test description")
                .build();
    }
}

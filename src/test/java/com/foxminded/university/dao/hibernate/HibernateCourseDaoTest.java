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
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;
import static com.foxminded.university.dao.hibernate.HibernateCourseDaoTest.TestData.*;

@SpringJUnitConfig(classes = TestConfig.class)
class HibernateCourseDaoTest {

    @Autowired
    private CourseDao courseDao;
    
    @Autowired   
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenNewCourse_whenCreate_thenCreateNewCourse() {
        courseDao.create(course1);

        Course actual = hibernateTemplate.get(Course.class, course1.getId());
        assertEquals(course1, actual);
    }

    @Test
    void givenCourseId_whenGetById_thenGetCourseById() {
        Course expected = course2;

        Course actual = courseDao.getById(course2.getId()).get();

        assertEquals(expected, actual);
    }
    
    @Test
    void givenCourseName_whenGetByName_thenGetCourseByName() {
        Course expected = course2;
        
        Course actual = courseDao.getByName("biology").get();
        
        assertEquals(expected, actual);
    }
    
    @Test
    void givenWongName_whenGetByName_thenReturnDefaultCourse() {
        Optional<Course> expected = Optional.empty();
        
        Optional<Course> actual = courseDao.getByName("Not existing name");;
        
        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableCourses_whenGetAll_thenGetAllCourses() {
        List<Course> expected = hibernateTemplate.loadAll(Course.class);
        
        List<Course> actual = courseDao.getAll();

        assertEquals(expected, actual);
    }
    
    @Test 
    void givenTableCourses_whenGetAll_theGetAllByPages() {
        List<Course> content = hibernateTemplate.loadAll(Course.class);
        Pageable page = PageRequest.of(0, 20);
        Page<Course> expected = new PageImpl<>(content, page, 5);
        
        Page<Course> actual = courseDao.getAll(page);
        
        assertEquals(expected, actual);
    }
    
    @Test 
    void givenNotEnoughData_whenGetAll_theReturnEmptyList() {
        List<Course> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<Course> expected = new PageImpl<Course>(content, page, 5);
        
        Page<Course> actual = courseDao.getAll(page);
        
        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenCourse_whenUpdate_thenUpdateCourse() {
        courseDao.update(course3);

        Course actual = hibernateTemplate.get(Course.class, course3.getId());
        assertEquals(course3, actual);
    }

    @Test
    void givenCourse_whenDelete_thenDeleteCourse() {
        Course course = course2;

        assertThrows(RuntimeException.class, () -> {
            courseDao.delete(course);
        });
    }
    
    @Test
    void givenTableCourses_whenGetCountOfRecordings_thenGetCountOfRecordings() {
        int expected = 5;
        
        int actual = courseDao.count();
        
        assertEquals(expected, actual);
    }

    interface TestData {
        Course course1 = new Course.Builder()
                .name("test name")
                .description("test description")
                .build();

        Course course2 = new Course.Builder()
                .id(1)
                .name("biology")
                .description("learning biology")
                .build();

        Course course3 = new Course.Builder()
                .id(1)
                .name("test")
                .description("test description")
                .build();

        Course course4 = new Course.Builder()
                .id(2)
                .name("math")
                .description("learning math")
                .build();

        Course course5 = new Course.Builder()
                .id(3)
                .name("history")
                .description("learning history")
                .build();

        Course course6 = new Course.Builder()
                .id(4)
                .name("sience")
                .description("learning sience")
                .build();
        
        Course course7 = new Course.Builder()
                .id(0)
                .name("Default")
                .description("Default")
                .build();
    }
}

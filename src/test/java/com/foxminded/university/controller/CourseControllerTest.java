package com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static com.foxminded.university.controller.CourseControllerTest.TestData.*;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import com.foxminded.university.model.Course;
import com.foxminded.university.service.CourseService;

@TestInstance(Lifecycle.PER_CLASS)
class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @Test
    void givenCourseController_whenGetAll_thenReturnCorrectView() throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        when(courseService.getAll()).thenReturn(courses);

        mockMvc.perform(get("/courses")).andExpect(view().name("course/courses"))
                .andExpect(model().attribute("courses", courses));
    }

    @Test
    void givenCourseController_whenGetById_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(courseService.getById(id)).thenReturn(course1);

        mockMvc.perform(get("/courses/{id}", id)).andExpect(view().name("course/info"))
                .andExpect(model().attribute("course", course1));
    }

    @Test
    void givenCourseController_whenCreateForm_thenCreateForm() throws Exception {
        mockMvc.perform(get("/courses/createForm").flashAttr("course", course1))
                .andExpect(view().name("course/create"))
                .andExpect(model().attribute("course", course1));
    }

    @Test
    void givenCourseController_whenCreate_thenCreate() throws Exception {
        mockMvc.perform(post("/courses/create").flashAttr("course", course1))
                .andExpect(view().name("redirect:/courses"));
        
        verify(courseService).create(course1);
    }

    @Test
    void givenCourseController_whenUpdateForm_thenUpdateForm() throws Exception {
        int id = 1;
        when(courseService.getById(id)).thenReturn(course1);

        mockMvc.perform(get("/courses/updateForm/{id}", id)).andExpect(view().name("course/update"))
                .andExpect(model().attribute("course", courseService.getById(id)));
    }

    @Test
    void givenCourseController_whenUpdate_thenUpdate() throws Exception {
        mockMvc.perform(post("/courses/update").flashAttr("course", course2))
                .andExpect(view().name("redirect:/courses"));
        
        verify(courseService).update(course2);
    }

    @Test
    void givenCourseController_whenDelete_thenDelete() throws Exception {
        int id = 1;
        Course course = new Course();
        course.setId(id);
        
        mockMvc.perform(get("/courses/delete/{id}", id).flashAttr("course", course1))
                .andExpect(view().name("redirect:/courses"));
        
        verify(courseService).delete(course1);
    }

    interface TestData {
        Course course1 = new Course.Builder()
                .id(0)
                .name("biology")
                .description("learning biology")
                .build();
        Course course2 = new Course.Builder()
                .id(2)
                .name("math")
                .description("learning math")
                .build();
    }
}

package com.foxminded.university.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;

import static com.foxminded.university.controller.CourseControllerTest.TestData.*;
import static com.foxminded.university.controller.GroupControllerTest.TestData.*;

@TestInstance(Lifecycle.PER_CLASS)
class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private GroupController groupController;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @Test
    void givenGroupController_whenGetAll_thenReturnCorrectView() throws Exception {
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        Page<Group> page = new PageImpl<Group>(groups);
        when(groupService.getAll(PageRequest.of(0, 20))).thenReturn(page);

        mockMvc.perform(get("/groups")).andExpect(view().name("group/groups"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void givenGroupController_whenShowInfo_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(groupService.getById(id)).thenReturn(group1);

        mockMvc.perform(get("/groups/{id}", id)).andExpect(view().name("group/info"))
                .andExpect(model().attribute("group", group1));
    }

    @Test
    void givenGroupController_whenCreateForm_thenCreateForm() throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        when(courseService.getAll()).thenReturn(courses);

        mockMvc.perform(get("/groups/createForm").flashAttr("group", group1)).andExpect(view().name("group/create"))
                .andExpect(model().attribute("group", group1))
                .andExpect(model().attribute("allCourses", courseService.getAll()));
    }

    @Test
    void givenGroupController_whenCreate_thenCreate() throws Exception {
        mockMvc.perform(post("/groups/create").flashAttr("group", group1)).andExpect(view().name("redirect:/groups"));
        
        verify(groupService).create(group1);
    }

    @Test
    void givenGroupController_whenUpdateForm_thenUpdateForm() throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        when(courseService.getAll()).thenReturn(courses);
        int id = 1;
        when(groupService.getById(id)).thenReturn(group1);

        mockMvc.perform(get("/groups/updateForm/{id}", id)).andExpect(view().name("group/update"))
                .andExpect(model().attribute("group", group1))
                .andExpect(model().attribute("allCourses", courseService.getAll()));

    }

    @Test
    void givenCourseController_whenUpdate_thenUpdate() throws Exception {
        mockMvc.perform(post("/groups/update").flashAttr("group", group1)).andExpect(view().name("redirect:/groups"));
        
        verify(groupService).update(group1);
    }

    @Test
    void givenGroupController_whenDelete_thenDelete() throws Exception {
        int id = 1;

        mockMvc.perform(get("/groups/delete/{id}", id).flashAttr("group", group1))
                .andExpect(view().name("redirect:/groups"));
        
        verify(groupService).delete(group1);
    }
    
    interface TestData {
        Group group1 = new Group.Builder()
                .id(1)
                .name("first group")
                .build();
        Group group2 = new Group.Builder()
                .id(2)
                .name("second group")
                .build();
    } 
}

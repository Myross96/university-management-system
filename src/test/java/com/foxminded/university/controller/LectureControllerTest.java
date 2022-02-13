package com.foxminded.university.controller;

import java.time.LocalDate;
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
import static com.foxminded.university.controller.LectureControllerTest.TestData.*;
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

import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.LectureTimeService;
import com.foxminded.university.service.TeacherService;

@TestInstance(Lifecycle.PER_CLASS)
class LectureControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LectureService lectureService;
    
    @Mock
    private TeacherService teacherService;
    
    @Mock
    private GroupService groupService;
    
    @Mock
    private CourseService courseService;
    
    @Mock
    private AudienceService audienceService;
    
    @Mock
    private LectureTimeService lectureTimeService;

    @InjectMocks
    private LectureController lectureController;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lectureController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void givenLectureControllers_whenGetAll_thenReturnCorrectView() throws Exception {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(lecture1);
        lectures.add(lecture2);
        Page<Lecture> page = new PageImpl<Lecture>(lectures);
        when(lectureService.getAll(PageRequest.of(0, 20))).thenReturn(page);

        mockMvc.perform(get("/lectures")).andExpect(view().name("lecture/lectures"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void givenLectureController_whenShowForm_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(lectureService.getById(id)).thenReturn(lecture1);

        mockMvc.perform(get("/lectures/{id}", id)).andExpect(view().name("lecture/info"))
                .andExpect(model().attribute("lecture", lecture1));
    }
    
    @Test
    void givenLectureController_whenCreateForm_thenCreateForm() throws Exception {
        when(teacherService.getAll()).thenReturn(new ArrayList<>());
        when(groupService.getAll()).thenReturn(new ArrayList<>());
        when(courseService.getAll()).thenReturn(new ArrayList<>());
        when(lectureTimeService.getAll()).thenReturn(new ArrayList<>());
        when(audienceService.getAll()).thenReturn(new ArrayList<>());
        
        mockMvc.perform(get("/lectures/createForm").flashAttr("lecture", lecture1))
                .andExpect(view().name("lecture/create")).andExpect(model().attribute("lecture", lecture1))
                .andExpect(model().attribute("allTeachers", teacherService.getAll()))
                .andExpect(model().attribute("allGroups", groupService.getAll()))
                .andExpect(model().attribute("allCourses", courseService.getAll()))
                .andExpect(model().attribute("allLecturesTimes", lectureTimeService.getAll()))
                .andExpect(model().attribute("allAudiences", audienceService.getAll()));                
    }
    
    @Test
    void givenLectureController_whenCreate_thenCreate() throws Exception {
        mockMvc.perform(post("/lectures/create").flashAttr("lecture", lecture1)).andExpect(view().name("redirect:/lectures"));
        
        verify(lectureService).create(lecture1);
    }
    
    @Test
    void givenLectureController_whenUpdateForm_thenUpdateForm() throws Exception {
        int id = 1;
        when(teacherService.getAll()).thenReturn(new ArrayList<>());
        when(groupService.getAll()).thenReturn(new ArrayList<>());
        when(courseService.getAll()).thenReturn(new ArrayList<>());
        when(lectureTimeService.getAll()).thenReturn(new ArrayList<>());
        when(audienceService.getAll()).thenReturn(new ArrayList<>());
        when(lectureService.getById(id)).thenReturn(lecture1);
        
        mockMvc.perform(get("/lectures/updateForm/{id}", id).flashAttr("lecture", lecture1))
                .andExpect(view().name("lecture/update")).andExpect(model().attribute("lecture", lecture1))
                .andExpect(model().attribute("allTeachers", teacherService.getAll()))
                .andExpect(model().attribute("allGroups", groupService.getAll()))
                .andExpect(model().attribute("allCourses", courseService.getAll()))
                .andExpect(model().attribute("allLecturesTimes", lectureTimeService.getAll()))
                .andExpect(model().attribute("allAudiences", audienceService.getAll()));
    }
    
    @Test
    void givenLectureController_whenUpdate_thenUpdate() throws Exception {
        mockMvc.perform(post("/lectures/update").flashAttr("lecture", lecture1)).andExpect(view().name("redirect:/lectures"));
        
        verify(lectureService).update(lecture1);
    }
    
    @Test
    void givenLectureController_whenDelete_thenDelete() throws Exception {
        int id = 1;

        mockMvc.perform(get("/lectures/delete/{id}", id).flashAttr("lecture", lecture1))
                .andExpect(view().name("redirect:/lectures"));
        
        verify(lectureService).delete(lecture1);
    }

    interface TestData {
        Lecture lecture1 = new Lecture.Builder()
                .id(1)
                .name("biology")
                .date(LocalDate.parse("2021-03-15"))
                .build();
        Lecture lecture2 = new Lecture.Builder()
                .id(2)
                .name("math")
                .date(LocalDate.parse("2021-03-17"))
                .build();
        
        LectureDto lectureDto1 = new LectureDto.Builder()
                .id(1)
                .title("Biology")
                .start("2021-03-15")
                .end("2021-03-15")
                .build();
        
        LectureDto lectureDto2 = new LectureDto.Builder()
                .id(2)
                .title("Math")
                .start("2021-03-15")
                .end("2021-03-15")
                .build();
    }
}

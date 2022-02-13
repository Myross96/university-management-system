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
import static com.foxminded.university.controller.TeacherControllerTest.TestData.*;
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

import com.foxminded.university.dto.ReassignTeachersDTO;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.TeacherService;

@TestInstance(Lifecycle.PER_CLASS)
class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;
    
    @Mock
    private GroupService groupService;
    
    @Mock
    private CourseService courseService;
    
    @Mock
    private LectureService lectureService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void givenTeahcerController_whenGetAll_thenReturnCorrectView() throws Exception {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);
        Page<Teacher> page = new PageImpl<>(teachers);
        when(teacherService.getAll(PageRequest.of(0, 20))).thenReturn(page);

        mockMvc.perform(get("/teachers")).andExpect(view().name("teacher/teachers"))
                .andExpect(model().attribute("page", page));

    }

    @Test
    void givenTeacherController_whewShowInfoForm_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(teacherService.getById(id)).thenReturn(teacher1);

        mockMvc.perform(get("/teachers/{id}", id)).andExpect(view().name("teacher/info"))
                .andExpect(model().attribute("teacher", teacher1));
    }
    
    @Test
    void givenTeacherController_whenCreateForm_thenReturnCorrectView() throws Exception {
        when(groupService.getAll()).thenReturn(new ArrayList<>());
        when(courseService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/teachers/createForm").flashAttr("teacher", teacher1))
                .andExpect(view().name("teacher/create")).andExpect(model().attribute("teacher", teacher1))
                .andExpect(model().attribute("allGroups", groupService.getAll()))
                .andExpect(model().attribute("allCourses", courseService.getAll()));
    }

    @Test
    void givenTeacherController_whenCreate_thenCreate() throws Exception {
        mockMvc.perform(post("/teachers/create").flashAttr("teacher", teacher1))
                .andExpect(view().name("redirect:/teachers"));
        
        verify(teacherService).create(teacher1);
    }
    
    @Test
    void givenTeacherController_whenUpdateForm_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(teacherService.getById(id)).thenReturn(teacher1);
        when(groupService.getAll()).thenReturn(new ArrayList<>());
        when(courseService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/teachers/updateForm/{id}", id))
                .andExpect(view().name("teacher/update")).andExpect(model().attribute("teacher", teacher1))
                .andExpect(model().attribute("allGroups", groupService.getAll()))
                .andExpect(model().attribute("allCourses", courseService.getAll()));
    }

    @Test
    void givenTeacherController_whenUpdate_thenUpdate() throws Exception {
        mockMvc.perform(post("/teachers/update").flashAttr("teacher", teacher1))
                .andExpect(view().name("redirect:/teachers"));
        
        verify(teacherService).update(teacher1);
    }
    
    @Test
    void givenTeacherController_whenDelete_thenDelete() throws Exception {
        int id = 1;

        mockMvc.perform(get("/teachers/delete/{id}", id).flashAttr("teacher", teacher1))
                .andExpect(view().name("redirect:/teachers"));
        
        verify(teacherService).delete(teacher1);
    }
    
    @Test
    void givenTeacherController_whenShowCalendar_threnReturnCorrectView() throws Exception {
        int teacherId = 1;
        
        mockMvc.perform(get("/teachers/{id}/calendar", teacherId)).andExpect(model().attribute("teacherId", teacherId))
        .andExpect(view().name("teacher/schedule"));
    }
    
    @Test
    void givenTeacherController_whenGetReassignForm_thenReturnCorrectView() throws Exception {
        int id = 1;
        ReassignTeachersDTO dto = new ReassignTeachersDTO();
        dto.setTeacherId(id);
        List<Teacher> avaliableTeachers = new ArrayList<>();
        avaliableTeachers.add(teacher1);  
        when(teacherService.getById(id)).thenReturn(teacher2);
        when(teacherService.getByCourses(teacher2.getCourses())).thenReturn(avaliableTeachers);
        
        mockMvc.perform(get("/teachers/{id}/reasignForm", id).flashAttr("reassignTeachersDTO", dto))
        .andExpect(model().attribute("avaliableTeachers", avaliableTeachers))
        .andExpect(view().name("teacher/reasign"));
    }
    
    @Test
    void givenTeacherController_whenReassignLectures_thenCallServiceMethod() throws Exception {
        ReassignTeachersDTO reassignData = new ReassignTeachersDTO();

        mockMvc.perform(post("/teachers/reasignLectures").flashAttr("reassignTeachersDTO", reassignData))
                .andExpect(view().name("redirect:/teachers"));

        verify(lectureService).replaceTeachers(reassignData.getTeacherId(), reassignData.getTeachers(),
                reassignData.getStart(), reassignData.getEnd());
    }

    interface TestData {
        Teacher teacher1 = new Teacher.Builder()
                .id(1)
                .firstName("Jenifer")
                .lastName("Davies")
                .birthDate(LocalDate.parse("1973-09-04"))
                .gender(Gender.FEMALE)
                .email("email1")
                .address("address1")
                .postCode("78542")
                .academicDegree(Degree.BACHELOR)
                .experience(12)
                .build();
        
        Teacher teacher2 = new Teacher.Builder()
                .id(2)
                .firstName("James")
                .lastName("Jones")
                .birthDate(LocalDate.parse("1973-09-04"))
                .gender(Gender.MALE)
                .email("email2")
                .address("address2")
                .postCode("42318")
                .academicDegree(Degree.MASTER)
                .experience(4)
                .build();
    }
}

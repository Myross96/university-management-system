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
import static com.foxminded.university.controller.StudentControllerTest.TestData.*;
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

import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.StudentService;

@TestInstance(Lifecycle.PER_CLASS)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;
    
    @Mock
    private GroupService groupService;
    
    @Mock
    private LectureService lectureService;

    @InjectMocks
    private StudentController studentController;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void givenStudentController_whenGetAll_thenReturnCorrectView() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        Page<Student> page = new PageImpl<>(students);
        when(studentService.getAll(PageRequest.of(0, 20))).thenReturn(page);

        mockMvc.perform(get("/students")).andExpect(view().name("student/students"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void givenStudentController_whenShowStudentForm_thenRetuntCorrectView() throws Exception {
        int id = 1;
        when(studentService.getById(id)).thenReturn(student1);

        mockMvc.perform(get("/students/{id}", id)).andExpect(view().name("student/info"))
                .andExpect(model().attribute("student", student1));
    }

    @Test
    void givenStudentController_whenCreateForm_thenReturnCorrectView() throws Exception {
        when(groupService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/students/createForm").flashAttr("student", student1))
                .andExpect(view().name("student/create")).andExpect(model().attribute("student", student1))
                .andExpect(model().attribute("allGroups", groupService.getAll()));
    }

    @Test
    void givenStudentController_whenCreate_thenCreate() throws Exception {
        mockMvc.perform(post("/students/create").flashAttr("student", student1))
                .andExpect(view().name("redirect:/students"));
        
        verify(studentService).create(student1);
    }

    @Test
    void givenStudentController_whenUpdateForm_thenReturnCorrectView() throws Exception {
        int id = 1;
        List<Group> groups = new ArrayList<>();
        when(groupService.getAll()).thenReturn(groups);

        mockMvc.perform(get("/students/updateForm/{id}", id))
                .andExpect(view().name("student/update"))
                .andExpect(model().attribute("allGroups", groups));
    }

    @Test
    void givenStudentController_whenUpdate_thenUpdate() throws Exception {
        mockMvc.perform(post("/students/update").flashAttr("student", student1))
                .andExpect(view().name("redirect:/students"));
        
        verify(studentService).update(student1);
    }

    @Test
    void givenStudentController_whenDelete_thenDelete() throws Exception {
        int id = 1;

        mockMvc.perform(get("/students/delete/{id}", id).flashAttr("student", student1))
                .andExpect(view().name("redirect:/students"));
        
        verify(studentService).delete(student1);
    }
    
    @Test
    void givenStudentController_whenShowCallendar_thenReturnCorrectView() throws Exception {
        int studentId = 1;
        
        mockMvc.perform(get("/students/{id}/calendar", studentId)).andExpect(model().attribute("studentId", studentId))
        .andExpect(view().name("student/schedule"));
    }
    
    @Test
    void givenStudentController_whenGetSchedule_thenGetSchedule() throws Exception {
        int id = 1;
        LocalDate start = LocalDate.parse("2021-03-01");
        LocalDate end = LocalDate.parse("2021-03-30");
        
        mockMvc.perform(get("/students/{id}/schedule", id).param("start", "2021-03-01", "end", "2021-03-30"));
        
        verify(lectureService).getByStudentAndMonth(id, start, end);
    }

    interface TestData {
        Student student1 = new Student.Builder()
                .id(1)
                .firstName("James")
                .lastName("Smith")
                .gender(Gender.MALE)
                .birthDate(LocalDate.parse("1995-07-05"))
                .address("address1")
                .email("email1")
                .postCode("13256")
                .build();
        Student student2 = new Student.Builder()
                .id(2)
                .firstName("Jennifer")
                .lastName("Jones")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.parse("1996-10-08"))
                .address("address2")
                .email("email2")
                .postCode("12487")
                .build();                
    }
}

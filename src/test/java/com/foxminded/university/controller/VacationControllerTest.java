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
import static org.mockito.Mockito.doNothing;

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

import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;

import static com.foxminded.university.controller.TeacherControllerTest.TestData.*;
import static com.foxminded.university.controller.VacationControllerTest.TestData.*;

@TestInstance(Lifecycle.PER_CLASS)
class VacationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VacationService vacationService;
    
    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private VacationController vacationController;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vacationController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void givenVacationController_wheGetAll_thenReturnCorrectView() throws Exception {
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        vacations.add(vacation1);
        Page<Vacation> page = new PageImpl<>(vacations);
        when(vacationService.getAll(PageRequest.of(0, 20))).thenReturn(page);

        mockMvc.perform(get("/vacations")).andExpect(view().name("vacation/vacations"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void givenVacationController_whenShowInfo_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(vacationService.getById(id)).thenReturn(vacation1);
        when(teacherService.getById(vacation1.getId())).thenReturn(teacher1);

        mockMvc.perform(get("/vacations/{id}", id)).andExpect(view().name("vacation/info"))
                .andExpect(model().attribute("vacation", vacation1));
    }
    
    @Test
    void givenVacationController_whenCreateForm_thenCreateForm() throws Exception {
        when(teacherService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/vacations/createForm")).andExpect(view().name("vacation/create"))
                .andExpect(model().attribute("vacation", vacation1))
                .andExpect(model().attribute("allTeachers", teacherService.getAll()));
    }

    @Test
    void givenVacationController_whenCreate_thenCreate() throws Exception {
        doNothing().when(vacationService).create(vacation1);

        mockMvc.perform(post("/vacations/create").flashAttr("vacation", vacation1))
                .andExpect(view().name("redirect:/vacations"));
    }
    
    @Test
    void givenVacationController_whenUpdateForm_thenUpdateForm() throws Exception {
        int id = 1;
        when(vacationService.getById(id)).thenReturn(vacation1);
        when(teacherService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/vacations/updateForm/{id}", id)).andExpect(view().name("vacation/update"))
                .andExpect(model().attribute("vacation", vacation1))
                .andExpect(model().attribute("allTeachers", teacherService.getAll()));
    }

    @Test
    void givenVacationController_whenUpdate_thenUpdate() throws Exception {
        doNothing().when(vacationService).update(vacation1);

        mockMvc.perform(post("/vacations/update").flashAttr("vacation", vacation1))
                .andExpect(view().name("redirect:/vacations"));
    }
    
    @Test
    void givenVacationController_whenDelete_thenDelete() throws Exception {
        int id = 1;
        doNothing().when(vacationService).delete(vacation1);

        mockMvc.perform(get("/vacations/delete/{id}", id).flashAttr("vacation", vacation1))
                .andExpect(view().name("redirect:/vacations"));
    }

    interface TestData {
        Vacation vacation1 = new Vacation.Builder()
                .id(1)
                .start(LocalDate.parse("2021-01-17"))
                .end(LocalDate.parse("2021-02-10"))
                .teacherId(1)
                .build();
        
        Vacation vacation2 = new Vacation.Builder()
                .id(2)
                .start(LocalDate.parse("2021-01-23"))
                .end(LocalDate.parse("2021-02-16"))
                .teacherId(2)
                .build();
    }
}

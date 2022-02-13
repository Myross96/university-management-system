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
import static com.foxminded.university.controller.HolidayControllerTest.TestData.*;
import static org.mockito.Mockito.verify;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.HolidayService;

@TestInstance(Lifecycle.PER_CLASS)
class HolidayControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidayController holidayController;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @Test
    void givenHolidayController_whenGetAll_thenReturenCorrectView() throws Exception {
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(holiday1);
        holidays.add(holiday2);
        when(holidayService.getAll()).thenReturn(holidays);

        mockMvc.perform(get("/holidays")).andExpect(view().name("holiday/holidays"))
                .andExpect(model().attribute("holidays", holidays));
    }

    @Test
    void givenHolidayController_whenShowInfoForm_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(holidayService.getById(id)).thenReturn(holiday1);

        mockMvc.perform(get("/holidays/{id}", id)).andExpect(view().name("holiday/info"))
                .andExpect(model().attribute("holiday", holiday1));
    }

    @Test
    void givenHolidayController_whenCreateForm_thenCreateForm() throws Exception {
        mockMvc.perform(get("/holidays/createForm").flashAttr("holiday", holiday1))
                .andExpect(view().name("holiday/create")).andExpect(model().attribute("holiday", holiday1));
    }

    @Test
    void givenHolidayController_wheCreate_thenCreate() throws Exception {
        mockMvc.perform(post("/holidays/create").flashAttr("holiday", holiday1))
                .andExpect(view().name("redirect:/holidays"));
        
        verify(holidayService).create(holiday1);
    }

    @Test
    void givenHolidayController_whenUpdateForm_thenUpdate() throws Exception {
        int id = 1;
        when(holidayService.getById(id)).thenReturn(holiday1);

        mockMvc.perform(get("/holidays/updateForm/{id}", id).flashAttr("holiday", holiday1))
                .andExpect(view().name("holiday/update")).andExpect(model().attribute("holiday", holiday1));
    }

    @Test
    void givenHolidayController_whenUpdate_thenUpdate() throws Exception {
        mockMvc.perform(post("/holidays/update").flashAttr("holiday", holiday1))
                .andExpect(view().name("redirect:/holidays"));
        
        verify(holidayService).update(holiday1);
    }

    @Test
    void givenHolidayController_whenDelete_thenDelete() throws Exception {
        int id = 1;
     
        mockMvc.perform(get("/holidays/delete/{id}", id).flashAttr("holiday", holiday1))
                .andExpect(view().name("redirect:/holidays"));
        
        verify(holidayService).delete(holiday1);
    }

    interface TestData {
        Holiday holiday1 = new Holiday.Builder()
                .id(1)
                .name("first holiday")
                .date(LocalDate.parse("2021-01-01"))
                .build();
        Holiday holiday2 = new Holiday.Builder()
                .id(2)
                .name("second holiday")
                .date(LocalDate.parse("2021-01-07"))
                .build();
    }
}

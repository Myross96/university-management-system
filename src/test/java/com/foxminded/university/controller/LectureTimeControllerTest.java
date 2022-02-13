package com.foxminded.university.controller;

import java.time.LocalTime;
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

import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;
import static com.foxminded.university.controller.LectureTimeControllerTest.TestData.*;

@TestInstance(Lifecycle.PER_CLASS)
class LectureTimeControllerTest {
    
    private MockMvc mockMvc;

    @Mock
    LectureTimeService lectureTimeService;
    
    @InjectMocks
    LectureTimeController lectureTimeController;
    
    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lectureTimeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }
    
    @Test
    void givenLectureTimeController_whenGetAll_thenGetReturnCorrectView() throws Exception {
        List<LectureTime> lecturesTimes = new ArrayList<>();
        lecturesTimes.add(lectureTime1);
        lecturesTimes.add(lectureTime2);
        Page<LectureTime> page = new PageImpl<LectureTime>(lecturesTimes);
        when(lectureTimeService.getAll(PageRequest.of(0, 20))).thenReturn(page);

        mockMvc.perform(get("/lectureTimes")).andExpect(view().name("lectureTime/lecturesTimes"))
                .andExpect(model().attribute("page", page));

    }

    @Test
    void givenLectureTimeController_whenGetById_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(lectureTimeService.getById(id)).thenReturn(lectureTime1);

        mockMvc.perform(get("/lectureTimes/{id}", id)).andExpect(view().name("lectureTime/info"))
                .andExpect(model().attribute("lectureTime", lectureTime1));
    }

    @Test
    void givenLectureTimeController_whenCreateForm_thenReturnCorrectView() throws Exception {
        mockMvc.perform(get("/lectureTimes/createForm").flashAttr("lectureTime", lectureTime1))
                .andExpect(view().name("lectureTime/create")).andExpect(model().attribute("lectureTime", lectureTime1));
    }

    @Test
    void givenLectureTimeController_whenCreate_thenCreate() throws Exception {
        mockMvc.perform(post("/lectureTimes/create").flashAttr("lectureTime", lectureTime1))
                .andExpect(view().name("redirect:/lectureTimes"));
        
        verify(lectureTimeService).create(lectureTime1);
    }
    
    @Test
    void givenLectureTimeController_whenUpdateForm_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(lectureTimeService.getById(id)).thenReturn(lectureTime1);

        mockMvc.perform(get("/lectureTimes/updateForm/{id}", id).flashAttr("lectureTime", lectureTime1))
                .andExpect(view().name("lectureTime/update")).andExpect(model().attribute("model", lectureTime1));
    }

    @Test
    void givenLectureTimeController_whenUpdate_thenUpdate() throws Exception {
        mockMvc.perform(post("/lectureTimes/update").flashAttr("lectureTime", lectureTime1))
                .andExpect(view().name("redirect:/lectureTimes"));
        
        verify(lectureTimeService).update(lectureTime1);
    }

    @Test
    void givenLectureTimeController_whenDelete_thenDelete() throws Exception {
        int id = 1;
       
        mockMvc.perform(get("/lectureTimes/delete/{id}", id).flashAttr("lectureTime", lectureTime1))
                .andExpect(view().name("redirect:/lectureTimes"));
        
        verify(lectureTimeService).delete(lectureTime1);
    }

    interface TestData {
        LectureTime lectureTime1 = new LectureTime.Builder()
                .id(1)
                .startTime(LocalTime.parse("08:30"))
                .endTime(LocalTime.parse("10:05"))
                .build();
        
        LectureTime lectureTime2 = new LectureTime.Builder()
                .id(2)
                .startTime(LocalTime.parse("10:20"))
                .endTime(LocalTime.parse("11:55"))
                .build();
    }
}

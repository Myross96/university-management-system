package com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static com.foxminded.university.controller.AudienceControllerTest.TestData.*;

import java.util.ArrayList;
import java.util.List;

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

import com.foxminded.university.model.Audience;
import com.foxminded.university.service.AudienceService;

@TestInstance(Lifecycle.PER_CLASS)
class AudienceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AudienceService audienceService;

    @InjectMocks
    private AudienceController audienceController;

    @BeforeAll
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(audienceController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void givinAudienceController_whenGetAll_thenReturnCorrectView() throws Exception {
        List<Audience> audiences = new ArrayList<>();
        audiences.add(audience1);
        audiences.add(audience2);
        Page<Audience> page = new PageImpl<>(audiences);
        when(audienceService.getAll(PageRequest.of(0, 20))).thenReturn(page);
  
        mockMvc.perform(get("/audiences")).andExpect(view().name("audience/audiences"))
                .andExpect(model().attribute("page", page));
    }

    @Test
    void givenAudienceController_whenGetById_thenReturnCorrectView() throws Exception {
        int id = 1;
        when(audienceService.getById(id)).thenReturn(audience1);

        mockMvc.perform(get("/audiences/{id}", id)).andExpect(view().name("audience/info"))
                .andExpect(model().attribute("audience", audience1));
    }
    
    @Test
    void givenAudienceController_whenCreateForm_thenReturnCreateForm() throws Exception {
        mockMvc.perform(get("/audiences/createForm")).andExpect(view().name("audience/create"))
                .andExpect(model().attribute("audience", new Audience()));
    }

    @Test
    void givenAudienceController_whenCreate_thenCreate() throws Exception {
        mockMvc.perform(post("/audiences/create")).andExpect(view().name("redirect:/audiences"));
        
        verify(audienceService).create(new Audience());
    }

    @Test
    void givenAudienceController_whenUpdateForm_thenReturnUpdateForm() throws Exception {
        int id = 1;
        when(audienceService.getById(id)).thenReturn(audience1);
        mockMvc.perform(get("/audiences/updateForm/{id}", id)).andExpect(view().name("audience/update"))
                .andExpect(model().attribute("audience", audience1));
    }

    @Test
    void givenAudienceController_whenUpdate_thenUpdate() throws Exception {
        mockMvc.perform(post("/audiences/update")).andExpect(view().name("redirect:/audiences"));
        
        verify(audienceService).update(new Audience());
    }

    @Test
    void givenAudienceController_whendelete_thenDelete() throws Exception {
        int id = 1;
        Audience audience = new Audience();
        audience.setId(id);
        mockMvc.perform(get("/audiences/delete/{id}", id)).andExpect(view().name("redirect:/audiences"));
        verify(audienceService).delete(audience);
    }

    interface TestData {
        Audience audience1 = new Audience.Builder()
                .id(1)
                .number(124)
                .capacity(45)
                .build();
        Audience audience2 = new Audience.Builder()
                .id(2)
                .number(234)
                .capacity(95)
                .build();
    }
}

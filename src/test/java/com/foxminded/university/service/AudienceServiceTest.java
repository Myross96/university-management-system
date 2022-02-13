package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.dao.AudienceDao;
import com.foxminded.university.exceptions.NotUniqueEntityException;
import com.foxminded.university.model.Audience;
import static com.foxminded.university.service.AudienceServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class AudienceServiceTest {

    @Mock
    private AudienceDao audienceDao;

    @InjectMocks
    private AudienceService audienceService;

    @Test
    void givenAudience_wehenCreate_thenCreate() {
        when(audienceDao.getByNumber(audience1.getNumber())).thenReturn(Optional.empty());

        audienceService.create(audience1);

        verify(audienceDao).create(audience1);
    }

    @Test
    void givenAlreadyExistAudiece_whenCreate_thenNotCreate() {
        when(audienceDao.getByNumber(audience1.getNumber())).thenReturn(Optional.of(audience2));
        Exception exception = assertThrows(NotUniqueEntityException.class, () -> audienceService.create(audience1));

        assertEquals("Number = '35' already exist", exception.getMessage());
    }

    @Test
    void givenId_whenGetById_thenGetById() {
        int id = 1;
        when(audienceDao.getById(id)).thenReturn(Optional.of(TestData.audience2));

        assertEquals(audience2, audienceService.getById(id));
    }

    @Test
    void givenAudiencesInStorage_whenGetAll_thenGetAll() {
        List<Audience> audiences = new ArrayList<>();
        audiences.add(audience2);
        when(audienceDao.getAll()).thenReturn(audiences);

        assertEquals(audiences, audienceService.getAll());
    }

    @Test
    void givenAudiencesInStorage_whenGetAll_thenGetAllByPages() {
        List<Audience> audiences = new ArrayList<>();
        audiences.add(audience2);
        Page<Audience> expected = new PageImpl<Audience>(audiences);
        when(audienceDao.getAll(PageRequest.of(1, 1))).thenReturn(expected);

        assertEquals(expected, audienceService.getAll(PageRequest.of(1, 1)));
    }

    @Test
    void givenAudience_whenUpdate_thenUpdate() {
        when(audienceDao.getByNumber(audience2.getNumber())).thenReturn(Optional.of(audience2));

        audienceService.update(audience2);

        verify(audienceDao).update(audience2);
    }

    @Test
    void givenAudience_whenDelete_thenDelete() {
        audienceService.delete(audience2);

        verify(audienceDao).delete(audience2);
    }

    interface TestData {
        Audience audience1 = new Audience.Builder()
                .number(35)
                .capacity(70)
                .build();
        
        Audience audience2 = new Audience.Builder()
                .id(1)
                .number(46)
                .capacity(1)
                .build();
        
        Audience audience3 = new Audience.Builder()
                .id(2)
                .number(53)
                .capacity(62)
                .build();
    }
}

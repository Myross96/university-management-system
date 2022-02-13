package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.TestConfig;
import com.foxminded.university.dao.AudienceDao;
import static com.foxminded.university.dao.hibernate.HibernateAudienceDaoTest.TestData.*;
import com.foxminded.university.model.Audience;

@SpringJUnitConfig(classes = TestConfig.class)
class HibernateAudienceDaoTest {

    @Autowired
    private AudienceDao audienceDao;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void giventNewAudiences_whenCreate_thenNewAudienceCreated() {
        audienceDao.create(audience1);

        Audience actual = hibernateTemplate.get(Audience.class, audience1.getId());
        assertEquals(audience1, actual);
    }

    @Test
    void givenAudienceId_whenGetById_thenGetAudienceById() {
        Audience expected = audience2;

        Audience actual = audienceDao.getById(audience2.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void givenAudienceNumber_whenGetByNumber_thenGetAudienceByNumber() {
        Audience expected = audience2;

        Audience actual = audienceDao.getByNumber(expected.getNumber()).get();

        assertEquals(expected, actual);
    }
    
    @Test
    void givenWrongNumber_wheGetByNumber_thenReturnEmptyOptional() {
        Optional<Audience> expected = Optional.empty();

        Optional<Audience> actual = audienceDao.getByNumber(-10);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableAudiences_whenGetAll_thenGetAll() {
        List<Audience> expected = hibernateTemplate.loadAll(Audience.class);

        List<Audience> actual = audienceDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableAudiences_whenGetAll_thenGetAllByPages() {
        List<Audience> content = hibernateTemplate.loadAll(Audience.class);
        Pageable page = PageRequest.of(0, 20);
        Page<Audience> expected = new PageImpl<>(content, page, 5);

        Page<Audience> actual = audienceDao.getAll(page);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenNotEnoughData_whenGetAll_thenReturnEmptyList() {
        List<Audience> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<Audience> expected = new PageImpl<>(content, page, 5);

        Page<Audience> actual = audienceDao.getAll(page);

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenAudience_whenUpdate_thenApdateAudience() {
        audienceDao.update(TestData.audience3);

        Audience actual = hibernateTemplate.get(Audience.class, audience3.getId());
        assertEquals(audience3, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenAudience_whenDelete_thenThrowRuntimeExpection() {
        assertThrows(RuntimeException.class, () -> {
            audienceDao.delete(audience2);
        });
    }
    
    @Test
    void givenTableAudiences_whenGetRecordingsCount_thenGetCountOfRecordings() {
        int expected = 5;
        
        int actual = audienceDao.count();
        
        assertEquals(expected, actual);
    }

    interface TestData {
        Audience audience1 = new Audience.Builder()
                .number(6)
                .capacity(65)
                .build();

        Audience audience2 = new Audience.Builder()
                .id(1)
                .number(124)
                .capacity(45)
                .build();

        Audience audience3 = new Audience.Builder()
                .id(1)
                .number(124)
                .capacity(50)
                .build();

        Audience audeince4 = new Audience.Builder()
                .id(4)
                .number(133)
                .capacity(60)
                .build();
        
        Audience audience5 = new Audience.Builder()
                .id(2)
                .number(234)
                .capacity(95)
                .build();
        
        Audience audience6  = new Audience.Builder()
                .id(0)
                .number(0)
                .capacity(0)
                .build();
    }
}

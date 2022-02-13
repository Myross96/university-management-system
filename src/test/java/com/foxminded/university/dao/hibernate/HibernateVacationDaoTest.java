package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.model.Vacation;
import static com.foxminded.university.dao.hibernate.HibernateVacationDaoTest.TestData.*;

@SpringJUnitConfig(classes = TestConfig.class)
class HibernateVacationDaoTest {

    @Autowired
    private VacationDao vacationDao;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenNewVacation_whenCreate_thenCreateVacation() {
        vacationDao.create(vacation1);

        Vacation actual = hibernateTemplate.get(Vacation.class, vacation1.getId());
        assertEquals(vacation1, actual);
    }

    @Test
    void givenVacationId_whenGetById_thenGetVacationById() {
        Vacation expected = vacation2;

        Vacation actual = vacationDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableVacations_whenGetAll_thenGetAll() {
        List<Vacation> expected = hibernateTemplate.loadAll(Vacation.class);

        List<Vacation> actual = vacationDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableVacations_whenGetAll_thenGetAllByPages() {
        List<Vacation> content = hibernateTemplate.loadAll(Vacation.class);
        Pageable page = PageRequest.of(0, 20);
        Page<Vacation> expected = new PageImpl<>(content, page, 5);

        Page<Vacation> actual = vacationDao.getAll(page);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenNotEnoughData_whenGetAll_thenReturnEmptyList() {
        List<Vacation> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<Vacation> expected = new PageImpl<>(content, page, 5);

        Page<Vacation> actual = vacationDao.getAll(page);

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenVacation_whenUpdate_thenUpdateVacation() {
        Vacation expected = vacation3;  

        vacationDao.update(expected);

        Vacation actual = hibernateTemplate.get(Vacation.class, expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    void givenVacation_whenDelete_thenThrowRuntimeException() {
        int expected = 4;
        
        vacationDao.delete(vacation2);
        
        int actual = vacationDao.count();
        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherId_whenGetByTeacherId_thenGetVacationByTeacherId() {
        List<Vacation> expected = new ArrayList<>();
        Vacation firstVacation = vacation2;
        Vacation secondVacation = vacation4;
        expected.add(firstVacation);
        expected.add(secondVacation);
        int teacherId = 1;

        List<Vacation> actual = vacationDao.getByTeacherId(teacherId);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableVacations_whenGetRecordingsCount_thenReturnRecordingsCount() {
        int expected = 5;
        
        int actual = vacationDao.count();
        
        assertEquals(expected, actual);
    }

    interface TestData {

        Vacation vacation1 = new Vacation.Builder()
                .start(LocalDate.parse("2021-06-17"))
                .end(LocalDate.parse("2021-06-30"))
                .teacherId(1)
                .build();

        Vacation vacation2 = new Vacation.Builder()
                .id(1)
                .start(LocalDate.parse("2021-01-17"))
                .end(LocalDate.parse("2021-02-10"))
                .teacherId(1)
                .build();

        Vacation vacation3 = new Vacation.Builder()
                .id(1)
                .start(LocalDate.parse("2021-07-17"))
                .end(LocalDate.parse("2021-08-10"))
                .teacherId(1)
                .build();

        Vacation vacation4 = new Vacation.Builder()
                .id(4)
                .start(LocalDate.parse("2021-04-17"))
                .end(LocalDate.parse("2021-05-02"))
                .teacherId(1)
                .build();
        
        Vacation vacation5 = new Vacation.Builder()
                .id(2)
                .start(LocalDate.parse("2021-01-23"))
                .end(LocalDate.parse("2021-02-16"))
                .teacherId(2)
                .build();
    }
}

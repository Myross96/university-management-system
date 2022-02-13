package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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
import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.model.Holiday;
import static com.foxminded.university.dao.hibernate.HibernateHolidayDaoTest.TestData.*;

@SpringJUnitConfig(classes = TestConfig.class)
class HibernateHolidayDaoTest {

    @Autowired
    private HolidayDao holidayDao;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenNewHoliday_whenCreate_thenCreateNewHoliday() {
        holidayDao.create(holiday1);

        Holiday actual = hibernateTemplate.get(Holiday.class, holiday1.getId());
        assertEquals(holiday1, actual);
    }

    @Test
    void givenHolidayId_whenGetById_thenGetHolidayById() {
        Holiday expected = holiday2;

        Holiday actual = holidayDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void givenHolidayDate_whenGetByDate_thenGetHolidayByDate() {
        Holiday expected = holiday2;

        Holiday actual = holidayDao.getByDate(LocalDate.parse("2021-01-01")).get();

        assertEquals(expected, actual);
    }
    
    @Test
    void givenWrongDate_whenGetByDate_thenReturnEmptyOptional() {
        Optional<Holiday> expected = Optional.empty();

        Optional<Holiday> actual = holidayDao.getByDate(LocalDate.parse("1935-01-01"));

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableHolidays_whenGetAll_thenGetAll() {
        List<Holiday> expected = hibernateTemplate.loadAll(Holiday.class);

        List<Holiday> actual = holidayDao.getAll();

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableHolidays_whenGetAll_theGetAllByPages() {
        List<Holiday> content = hibernateTemplate.loadAll(Holiday.class);
        Pageable page = PageRequest.of(0, 20);
        Page<Holiday> expected = new PageImpl<>(content, page, 5);
        
        Page<Holiday> actual = holidayDao.getAll(page);
        
        assertEquals(expected, actual);
    }
    
    @Test
    void givenNotEnoughData_whenGetAll_theReturnEmtyList() {
        List<Holiday> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<Holiday> expected = new PageImpl<>(content, page, 5);
        
        Page<Holiday> actual = holidayDao.getAll(page);
        
        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableHolidays_whenGetCountOfRecordings_thenGetCountOfRecordings() {
        int expected = 5;
        
        int actual = holidayDao.count();
        
        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenHoliday_whenUpdate_thenUpdateHoliday() {
        Holiday expected = holiday3;

        holidayDao.update(expected);

        Holiday actual = hibernateTemplate.get(Holiday.class, expected.getId());
        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenHolida_whenDelete_thenThrowRuntimeException() {
        Holiday holiday = holiday2;
        int expected = 4;
        
        holidayDao.delete(holiday);
        
        int actual = holidayDao.count();
        assertEquals(expected, actual);
    }

    interface TestData {

        Holiday holiday1 = new Holiday.Builder()
                .name("test")
                .date(LocalDate.parse("2021-10-01"))
                .build();

        Holiday holiday2 = new Holiday.Builder()
                .id(1)
                .name("first holiday")
                .date(LocalDate.parse("2021-01-01"))
                .build();

        Holiday holiday3 = new Holiday.Builder()
                .id(1)
                .name("test name")
                .date(LocalDate.parse("2021-06-21"))
                .build();
        
        Holiday holiday4 = new Holiday.Builder()
                .id(0)
                .name("Default")
                .date(LocalDate.parse("2000-01-01"))
                .build();
    }
}

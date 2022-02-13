package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.exceptions.NotUniqueEntityException;
import com.foxminded.university.model.Holiday;
import static com.foxminded.university.service.HolidayServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayDao holidayDao;

    @InjectMocks
    private HolidayService holidayService;

    @Test
    void givenHoliday_whenCreate_thenCreate() {
        when(holidayDao.getByDate(holiday1.getDate())).thenReturn(Optional.empty());

        holidayService.create(holiday1);

        verify(holidayDao).create(holiday1);
    }

    @Test
    void givenAlreadyExistHoliday_whenCreate_thenNotCreate() {
        when(holidayDao.getByDate(holiday1.getDate())).thenReturn(Optional.of(holiday2));
        Exception exception = assertThrows(NotUniqueEntityException.class, () -> holidayService.create(holiday1));

        assertEquals("Name = 'test name' already exist", exception.getMessage());
    }

    @Test
    void givenCorrectId_whenGetById_thenGetById() {
        Holiday expected = holiday2;
        int id = expected.getId();
        when(holidayDao.getById(id)).thenReturn(Optional.ofNullable(expected));

        Holiday actual = holidayService.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    void givenTableHolidays_whenGetAll_thenGetAll() {
        List<Holiday> expected = new ArrayList<>();
        expected.add(holiday2);
        when(holidayDao.getAll()).thenReturn(expected);

        List<Holiday> actual = holidayService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenHoliday_whenUpdate_thenUpdate() {
        when(holidayDao.getByDate(holiday2.getDate())).thenReturn(Optional.of(holiday2));

        holidayService.update(holiday2);

        verify(holidayDao).update(holiday2);
    }

    @Test
    void givenHoliday_whenDelete_thenDelete() {
        holidayService.delete(holiday2);

        verify(holidayDao).delete(holiday2);
    }

    interface TestData {
        Holiday holiday1 = new Holiday.Builder()
                .name("test name")
                .date(LocalDate.parse("2021-06-25"))
                .build();

        Holiday holiday2 = new Holiday.Builder()
                .id(1)
                .name("holiday2")
                .date(LocalDate.parse("2021-04-02"))
                .build();
        
        Holiday holiday3 = new Holiday.Builder()
                .name("holiday2")
                .date(LocalDate.parse("2021-05-23"))
                .build();
        
        Holiday holiday4 = new Holiday.Builder()
                .name("holiday4")
                .date(LocalDate.parse("2021-05-20"))
                .build();
    }
}

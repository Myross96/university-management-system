package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.exceptions.IncorrectVacationPeriodException;
import com.foxminded.university.exceptions.TeacherIsBusyException;
import com.foxminded.university.model.Degree;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Vacation;
import static com.foxminded.university.service.VacationServiceTest.TestData.*;
import static com.foxminded.university.service.TeacherServiceTest.TestData.*;
import static com.foxminded.university.service.LectureServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class VacationServiceTest {

    @Mock
    private VacationDao vacationDao;
    @Mock
    private TeacherDao teacherDao;
    @Mock
    private LectureDao lectureDao;
    @Mock
    private UniversityConfigProperties properties;
    @InjectMocks
    private VacationService vacationService;

    private Map<Degree, Integer> vacationDays;

    @BeforeEach
    void init() {
        vacationDays = new EnumMap<>(Degree.class);
        vacationDays.put(Degree.BACHELOR, 15);
        vacationDays.put(Degree.MASTER, 20);
        vacationDays.put(Degree.DOCTOR, 25);
        
        lenient().when(properties.getVacationDays()).thenReturn(vacationDays);
    }

    @Test
    void givenVacation_whenCreate_thenCreateVacation() {
        when(lectureDao.getByTeacherAndDateRange(vacation1.getTeacherId(), vacation1.getStart(), vacation1.getEnd()))
                .thenReturn(new ArrayList<Lecture>());
        when(teacherDao.getById(vacation1.getTeacherId())).thenReturn(Optional.of(teacher2));

        vacationService.create(vacation1);

        verify(vacationDao).create(vacation1);
    }

    @Test
    void givenWrongVacationDuration_whenCreate_thenThrowIncorrectVacationPeriodException() {
        when(teacherDao.getById(vacation4.getTeacherId())).thenReturn(Optional.of(teacher2));
        
        Exception exception = assertThrows(IncorrectVacationPeriodException.class,
                () -> vacationService.create(vacation4));

        assertEquals("Period '2021-05-05 - 2021-05-26' to long", exception.getMessage());
    }

    @Test
    void givenTeacherHasLecturesForPeriod_whenCreate_thenThrowIncorrectVacationPeriodException() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(lecture2);
        when(lectureDao.getByTeacherAndDateRange(vacation1.getTeacherId(), vacation1.getStart(), vacation1.getEnd()))
                .thenReturn(lectures);
        when(teacherDao.getById(vacation1.getTeacherId())).thenReturn(Optional.of(teacher2));
        
        Exception exception = assertThrows(TeacherIsBusyException.class, () -> vacationService.create(vacation1));

        assertEquals("Teacher 'teacher2 teacher2' has lecture for period - '2021-05-12-2021-05-26'",
                exception.getMessage());
    }

    @Test
    void givenCorrectId_whenGetById_thenGetById() {
        Vacation expected = vacation2;
        int id = expected.getId();
        when(vacationDao.getById(id)).thenReturn(Optional.ofNullable(expected));

        Vacation actual = vacationService.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    void givenVacationsInStorage_whenGetAll_thenGetAll() {
        List<Vacation> expected = new ArrayList<>();
        expected.add(vacation2);
        when(vacationDao.getAll()).thenReturn(expected);

        List<Vacation> actual = vacationService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenVacationsInStorage_whenGetAll_thenGetAllByPages() {
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        Page<Vacation> expected = new PageImpl<>(vacations);
        PageRequest request = PageRequest.of(1, 1);
        when(vacationDao.getAll(request)).thenReturn(expected);
        
        assertEquals(expected, vacationService.getAll(request));
    }

    @Test
    void givenVacation_whenUpdate_thenUpdateVacation() {
        when(teacherDao.getById(vacation2.getTeacherId())).thenReturn(Optional.of(teacher2));

        vacationService.update(vacation2);

        verify(vacationDao).update(vacation2);
    }

    @Test
    void givenVacation_whenDelete_thenDelete() {
        vacationService.delete(vacation2);

        verify(vacationDao).delete(vacation2);
    }
    
    interface TestData {
        Vacation vacation1 = new Vacation.Builder()
                .start(LocalDate.parse("2021-05-12"))
                .end(LocalDate.parse("2021-05-26"))
                .teacherId(1)
                .build();
        
        Vacation vacation2 = new Vacation.Builder()
                .id(2)
                .start(LocalDate.parse("2021-07-12"))
                .end(LocalDate.parse("2021-07-26"))
                .build();
        
        Vacation vacation3 = new Vacation.Builder()
                .start(LocalDate.parse("2021-05-12"))
                .end(LocalDate.parse("2021-05-26"))
                .build();
        
        Vacation vacation4 = new Vacation.Builder()
                .start(LocalDate.parse("2021-05-05"))
                .end(LocalDate.parse("2021-05-26"))
                .build();
    }
}

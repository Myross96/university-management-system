package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.foxminded.university.service.LectureTimeServiceTest.TestData.*;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.exceptions.InvalidLectureTimeException;
import com.foxminded.university.model.LectureTime;

@ExtendWith(MockitoExtension.class)
class LectureTimeServiceTest {

    @Mock
    private LectureTimeDao lectureTimeDao;
    @Mock
    private UniversityConfigProperties properties;

    @InjectMocks
    private LectureTimeService lectureTimeService;

    @BeforeEach
    private void initUniversityConfig() {
        lenient().when(properties.getMinLectureDurationInMinutes()).thenReturn(30);
    }
    
    @Test
    void givenLectureTime_whenCreate_thenCreate() {
        when(properties.getMinLectureDurationInMinutes()).thenReturn(30);

        lectureTimeService.create(lectureTime1);

        verify(lectureTimeDao).create(lectureTime1);
    }

    @Test
    void givenWrongLectureDuration_whenCreate_thenThrowLowLectureTimeDuration() {
        Exception exception = assertThrows(InvalidLectureTimeException.class,
                () -> lectureTimeService.create(lectureTime3));

        assertEquals("time - '10:35 - 10:50' not valid", exception.getMessage());
    }

    @Test
    void givenEndLectureBeforeStartLecture_whenCreate_thenThrowIncorrectLectureTimeException() {
        Exception exception = assertThrows(InvalidLectureTimeException.class,
                () -> lectureTimeService.create(lectureTime4));

        assertEquals("time - '10:50 - 10:00' not valid", exception.getMessage());
    }

    @Test
    void givenCorrectId_whenGetById_thenGetById() {
        LectureTime expected = lectureTime2;
        int id = expected.getId();
        when(lectureTimeDao.getById(id)).thenReturn(Optional.ofNullable(expected));

        LectureTime actual = lectureTimeService.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    void givenTableLectureTimesWithData_whenGetAll_thenGetAll() {
        List<LectureTime> expected = new ArrayList<>();
        expected.add(lectureTime2);
        when(lectureTimeDao.getAll()).thenReturn(expected);

        List<LectureTime> actual = lectureTimeService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenLectureTime_whenUpdate_thenUpdate() {
        lectureTimeService.update(lectureTime2);

        verify(lectureTimeDao).update(lectureTime2);
    }

    @Test
    void givenLectureTime_whenDelete_thenDelete() {
        lectureTimeService.delete(lectureTime2);

        verify(lectureTimeDao).delete(lectureTime2);
    }
    
    interface TestData {
        LectureTime lectureTime1 = new LectureTime.Builder()
                .startTime(LocalTime.parse("08:30"))
                .endTime(LocalTime.parse("10:20"))
                .build();
        
        LectureTime lectureTime2 = new LectureTime.Builder()
                .id(2)
                .startTime(LocalTime.parse("10:35"))
                .endTime(LocalTime.parse("11:05"))
                .build();
        
        LectureTime lectureTime3 = new LectureTime.Builder()
                .id(2)
                .startTime(LocalTime.parse("10:35"))
                .endTime(LocalTime.parse("10:50"))
                .build();
        
        LectureTime lectureTime4 = new LectureTime.Builder()
                .id(2)
                .startTime(LocalTime.parse("10:50"))
                .endTime(LocalTime.parse("10:00"))
                .build();
    }
}

package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
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
import com.foxminded.university.dao.LectureTimeDao;
import com.foxminded.university.model.LectureTime;
import static com.foxminded.university.dao.hibernate.HibernateLectureTimeDaoTest.TestData.*;

@SpringJUnitConfig(classes = TestConfig.class)
class HibernateLectureTimeDaoTest {

    @Autowired
    private LectureTimeDao lectureTimeDao;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenNewLectureTime_whenCreate_thenCreateNewLectureTime() {
        lectureTimeDao.create(lectureTime1);

        LectureTime actual = hibernateTemplate.get(LectureTime.class, lectureTime1.getId());

        assertEquals(lectureTime1, actual);
    }

    @Test
    void givenLecturesTimeId_whenGetById_thenGetLectureTimeById() {
        LectureTime expected = lectureTime2;

        LectureTime actual = lectureTimeDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableLecturesTimes_whenGetAll_thenGetAll() {
        List<LectureTime> expected = hibernateTemplate.loadAll(LectureTime.class);

        List<LectureTime> actual = lectureTimeDao.getAll();

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableLecturesTimes_whenGetAll_thenAllByPages() {
        List<LectureTime> content = hibernateTemplate.loadAll(LectureTime.class);
        Pageable page = PageRequest.of(0, 20);
        Page<LectureTime> expected = new PageImpl<>(content, page, 5);

        Page<LectureTime> actual = lectureTimeDao.getAll(page);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenNotEnoughData_whenGetAll_thenReturnEmptyList() {
        List<LectureTime> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<LectureTime> expected = new PageImpl<>(content, page, 5);

        Page<LectureTime> actual = lectureTimeDao.getAll(page);

        assertEquals(expected, actual);
    }

    @Test
    void givenLecturesTime_whenUpdate_thenUpdateLectureTime() {
        LectureTime expected = lectureTime3;

        lectureTimeDao.update(expected);

        LectureTime actual = hibernateTemplate.get(LectureTime.class, expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    void givenLecturesTime_whenDelete_thenThrowRuntimeException() {
        LectureTime lectureTime = lectureTime2;

        assertThrows(RuntimeException.class, () -> {
            lectureTimeDao.delete(lectureTime);
        });
    }
    
    @Test
    void givenLectureTime_whenGetRecordingsCount_thenReturnCount() {
        int expected = 5;
        
        int actual = lectureTimeDao.count();
        
        assertEquals(expected, actual);
    }

    interface TestData {

        LectureTime lectureTime1 = new LectureTime.Builder()
                .startTime(LocalTime.parse("17:55"))
                .endTime(LocalTime.parse("19:20"))
                .build();

        LectureTime lectureTime2 = new LectureTime.Builder()
                .id(1)
                .startTime(LocalTime.parse("08:30"))
                .endTime(LocalTime.parse("10:05"))
                .build();

        LectureTime lectureTime3 = new LectureTime.Builder()
                .id(1)
                .startTime(LocalTime.parse("07:30"))
                .endTime(LocalTime.parse("09:05"))
                .build();

        LectureTime lectureTime4 = new LectureTime.Builder()
                .id(3)
                .startTime(LocalTime.parse("12:10"))
                .endTime(LocalTime.parse("09:05"))
                .build();
        
        LectureTime lectureTime5 = new LectureTime.Builder()
                .id(2)
                .startTime(LocalTime.parse("10:20"))
                .endTime(LocalTime.parse("11:55"))
                .build();                
    }
}    

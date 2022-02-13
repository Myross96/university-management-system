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
import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import static com.foxminded.university.dao.hibernate.HibernateLectureDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateCourseDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateGroupDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateLectureTimeDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateTeacherDaoTest.TestData.*;
import static com.foxminded.university.dao.hibernate.HibernateAudienceDaoTest.TestData.*;

@SpringJUnitConfig(classes = TestConfig.class)
class HibernateLectureDaoTest {
    
    @Autowired
    private LectureDao lectureDao;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenNewLecture_whenCreate_thenCreateNewLecture() {
        List<Group> groups = new ArrayList<>();
        groups.add(group2);

        Lecture lecture = lecture1;
        lecture.setGroups(groups);

        lectureDao.create(lecture);

        Lecture actual = hibernateTemplate.get(Lecture.class, lecture.getId());

        assertEquals(lecture, actual);
    }

    @Test
    void givenLectureId_whenGetById_thenGetLectureById() {
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        groups.add(group4);

        Lecture expected = TestData.lecture2;
        expected.setGroups(groups);

        Lecture actual = lectureDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableLectures_whenGetAll_thenGetAll() {
        List<Lecture> expected = hibernateTemplate.loadAll(Lecture.class);

        List<Lecture> actual = lectureDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void givenTableLectures_whenGetAll_thenGetAllByPages() {
        List<Lecture> content = hibernateTemplate.loadAll(Lecture.class);
        Pageable page = PageRequest.of(0, 20);
        Page<Lecture> expected = new PageImpl<Lecture>(content, page, 5);

        Page<Lecture> actual = lectureDao.getAll(page);

        assertEquals(expected, actual);
    }
    
    @Test
    void givenNotEnoughData_whenGetAll_thenReturnEmptyList() {
        List<Lecture> content = new ArrayList<>();
        Pageable page = PageRequest.of(1, 20);
        Page<Lecture> expected = new PageImpl<Lecture>(content, page, 5);

        Page<Lecture> actual = lectureDao.getAll(page);

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenLecture_whenUpdate_thenUpdateLecture() {
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        groups.add(group4);
        Lecture expected = lecture3;
        expected.setGroups(groups);

        lectureDao.update(expected);

        Lecture actual = hibernateTemplate.get(Lecture.class, expected.getId());
        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void givenLecture_whenDelete_thenDeleteLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(1);
        int expected = 4;

        lectureDao.delete(lecture);

        int actual = lectureDao.count();
        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherIdAndDate_wheGetByTeacherAndDate_thenGetLectureByTeacherAndDate() {
        int teacherId = 1;
        int expected = 1;

        int actual = lectureDao.getByTeacherAndDate(teacherId, LocalDate.parse("2021-03-15")).size();

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherIdAndStartAndEndMonth_whenGetByTeacherAndMonth_thenGetLecturesByTeacherIdAndMonth() {
        int teacherId = 1;
        int expected = 2;

        int actual = lectureDao
                .getByTeacherAndDateRange(teacherId, LocalDate.parse("2021-03-01"), LocalDate.parse("2021-03-30")).size();

        assertEquals(expected, actual);
    }

    @Test
    void givenStudentIdAndDate_wheGetByStudentAndDate_thenGetLecturesByStudentAndDate() {
        int studentId = 1;
        int expected = 1;

        int actual = lectureDao.getByStudentAndDate(studentId, LocalDate.parse("2021-03-15")).size();

        assertEquals(expected, actual);
    }

    @Test
    void givenStudentIdStartMonthAndEndMonth_wheGetByStudentAndMonth_thenGetLecturesByStudentAndMonth() {
        int studentId = 1;
        int expected = 2;

        int actual = lectureDao
                .getByStudentAndDateRange(studentId, LocalDate.parse("2021-03-01"), LocalDate.parse("2021-03-30")).size();

        assertEquals(expected, actual);
    }

    @Test
    void givenGroupIdDateAndLectureTimeId_whenGetByGroupDateAndLectureTimeId_thenGetLecturesByGroupAndDate() {
        int groupId = 1;
        int lectureTimeId = 2;
        List<Group> groups = new ArrayList<>();
        groups.add(group2);

        Lecture expected = lecture4;
        expected.setGroups(groups);

        Lecture actual = lectureDao.getByGroupDateAndLectureTime(groupId, LocalDate.parse("2021-03-17"), lectureTimeId)
                .get();

        assertEquals(expected, actual);
    }

    @Test
    void givenAudienceIdDateAndLectureTimeId_whenGetByAudienceIdDateAndLectureTimeId_thenGetByAudienceIdAndDate() {
        int audienceId = 2;
        int lectureTimeId = 2;
        List<Group> groups = new ArrayList<>();
        groups.add(group2);

        Lecture expected = lecture4;
        expected.setGroups(groups);

        Lecture actual = lectureDao
                .getByAudienceDateAndLectureTime(audienceId, LocalDate.parse("2021-03-17"), lectureTimeId).get();


        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherIdDateAndLectureTimeId_whenGetByTeacheDateAndLectureTime_thenGetByTeacherDateAndLectureTime() {
        int teacherId = 1;
        int lectureTimeId = 1;
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        groups.add(group4);

        Lecture expected = lecture2;

        Lecture actual = lectureDao
                .getByTeacherDateAndLectureTime(teacherId, LocalDate.parse("2021-03-15"), lectureTimeId).get();

        assertEquals(expected, actual);
    }
    
    @Test
    void givenTableLectures_whenGetRecordingsCount_thenGetCountOfRecordings() {
        int expected = 5;
        
        int actual = lectureDao.count();
        
        assertEquals(expected, actual);
    }

    interface TestData {

        Lecture lecture1 = new Lecture.Builder()
                .name("TestName")
                .date(LocalDate.parse("2021-03-22"))
                .audience(audience2)
                .course(course4)
                .teacher(teacher2)
                .lectureTime(lectureTime2)
                .build();

        Lecture lecture2 = new Lecture.Builder()
                .id(1)
                .name("biology")
                .date(LocalDate.parse("2021-03-15"))
                .audience(audience2)
                .course(course2)
                .teacher(teacher2)
                .lectureTime(lectureTime2)
                .build();

        Lecture lecture3 = new Lecture.Builder()
                .id(1)
                .name("sience")
                .date(LocalDate.parse("2021-03-15"))
                .teacher(teacher4)
                .course(course6)
                .audience(audeince4)
                .lectureTime(lectureTime5)
                .build();
        
        Lecture lecture4 = new Lecture.Builder()
                .id(2)
                .name("math")
                .date(LocalDate.parse("2021-03-17"))
                .teacher(teacher4)
                .course(course4)
                .audience(audience5)
                .lectureTime(lectureTime5)
                .build();
    }
}

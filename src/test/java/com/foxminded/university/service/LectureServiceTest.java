package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

import java.time.LocalDate;
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

import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dto.ReassignTeachersDTO;
import com.foxminded.university.exceptions.AudienceOccupiedException;
import com.foxminded.university.exceptions.GroupDoesNotLearnLectureCourseException;
import com.foxminded.university.exceptions.GroupOnAnotherLectureException;
import com.foxminded.university.exceptions.LectureNotReassignException;
import com.foxminded.university.exceptions.LectureOnHolidayException;
import com.foxminded.university.exceptions.LectureOnWeekendException;
import com.foxminded.university.exceptions.NotEnoughAudienceCapacityException;
import com.foxminded.university.exceptions.NotQualifiedTeacherException;
import com.foxminded.university.exceptions.TeacherInVacationException;
import com.foxminded.university.exceptions.NotAvailableTeacherException;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import static com.foxminded.university.service.AudienceServiceTest.TestData.*;
import static com.foxminded.university.service.CourseServiceTest.TestData.*;
import static com.foxminded.university.service.GroupServiceTest.TestData.*;
import static com.foxminded.university.service.HolidayServiceTest.TestData.*;
import static com.foxminded.university.service.LectureTimeServiceTest.TestData.*;
import static com.foxminded.university.service.StudentServiceTest.TestData.*;
import static com.foxminded.university.service.VacationServiceTest.TestData.*;
import static com.foxminded.university.service.LectureServiceTest.TestData.*;
import static com.foxminded.university.service.TeacherServiceTest.TestData.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureDao lectureDao;
    @Mock
    private HolidayDao holidayDao;

    @InjectMocks
    private LectureService lectureService;

    @Test
    void givenCorrectLecture_whenCreate_thenCreate() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());

        lectureService.create(lecture1);

        verify(lectureDao).create(lecture1);
    }

    @Test
    void givenGroupOnAnotherLecture_whenCreate_thenThrowGroupOnAnotherLectureException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(),
                lecture1.getDate(), lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.of(lecture3));
        Exception exception = assertThrows(GroupOnAnotherLectureException.class, () -> lectureService.create(lecture1));
        String expected = "One or more groups have lectures at - '10:35-11:05'";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenGroupNotLearnLectureCourse_whenCreate_thenThrowGroupDoesNotLearnLectureCourseException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course3);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(),
                lecture1.getDate(), lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(GroupDoesNotLearnLectureCourseException.class,
                () -> lectureService.create(lecture1));
        String expected = "One or more groups does't learn course - 'course2'";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenWrongQualifiedTeacher_whenCreate_thenThrowNotQualifiedTeacherException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(new ArrayList<Course>());
        lecture1.getTeacher().getCourses().add(course3);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(),
                lecture1.getDate(), lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotQualifiedTeacherException.class, () -> lectureService.create(lecture1));
        String expected = "Teacher - 'teacher1 teacher1' not qualified for lecture - 'testName'";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherInVacation_whenCreate_thenThrowTeacherInVacationException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation3);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(),
                lecture1.getDate(), lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(TeacherInVacationException.class, () -> lectureService.create(lecture1));
        String expected = "Teacher 'teacher1 teacher1' in vacation";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenTeacherOnAnotherLecture_whenCreate_thenThrowNewTeacherNotCreatedException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.of(lecture3));
        lenient().when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(),
                lecture1.getDate(), lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotAvailableTeacherException.class, () -> lectureService.create(lecture1));
        String expected = "Teacher - 'teacher1 teacher1' on another lecture";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenLectureInHoliday_whenCreate_thenThrowLectureOnHolidayException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.of(holiday4));
        lenient().when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(),
                lecture1.getDate(), lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(LectureOnHolidayException.class, () -> lectureService.create(lecture1));
        String expected = "Date - '2021-05-20' is a holiday";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenLowAudienceCapacity_whenCreate_thenThrowNotInafAudienceCapacityException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        students.add(student3);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        group2.setCourses(courses);
        group2.setStudents(students);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(),
                lecture1.getDate(), lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        lenient().when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotEnoughAudienceCapacityException.class,
                () -> lectureService.create(lecture1));
        String expected = "Audience - '1' doesn't has inaf capacity";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenAudienceOccupied_whenCreate_thenThrowAudienceOccupiedException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.of(lecture3));
        when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(AudienceOccupiedException.class, () -> lectureService.create(lecture1));
        String expected = "Audience - '46' occupied";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenLectureOnWeekend_whenCreate_thenThrowLectureOnWeekendException() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture4.setGroups(groups);
        lecture4.getTeacher().setVacations(vacations);
        lecture4.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture4.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherDateAndLectureTime(lecture4.getTeacher().getId(), lecture4.getDate(),
                lecture4.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByAudienceDateAndLectureTime(lecture4.getAudience().getCapacity(), lecture4.getDate(),
                lecture4.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateAndLectureTime(lecture4.getGroups().get(0).getId(), lecture4.getDate(),
                lecture4.getLectureTime().getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(LectureOnWeekendException.class, () -> lectureService.create(lecture4));
        String expected = "'2021-05-23' is a weekend";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void givenCorrectId_whenGetById_thenGetById() {
        Lecture expected = lecture2;
        int id = 2;
        when(lectureDao.getById(id)).thenReturn(Optional.ofNullable(expected));

        Lecture actual = lectureService.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    void givenTableLectureWithData_whenGetAll_thenGetAll() {
        List<Lecture> expected = new ArrayList<>();
        expected.add(lecture2);
        when(lectureDao.getAll()).thenReturn(expected);

        lectureService.getAll();

        verify(lectureDao).getAll();
    }

    @Test
    void givenTableLectureWithData_whenGetAll_thenGetAllByPages() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(lecture2);
        Page<Lecture> expected = new PageImpl<>(lectures);
        when(lectureDao.getAll(PageRequest.of(1, 1))).thenReturn(expected);

        assertEquals(expected, lectureService.getAll(PageRequest.of(1, 1)));
    }

    @Test
    void givenCorrectLecture_whenUpdate_whenUpdate() {
        List<Student> students = new ArrayList<>();
        students.add(student2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        List<Group> groups = new ArrayList<>();
        groups.add(group2);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(vacation2);
        groups.get(0).setCourses(courses);
        groups.get(0).setStudents(students);
        lecture1.setId(2);
        lecture1.setGroups(groups);
        lecture1.getTeacher().setVacations(vacations);
        lecture1.getTeacher().setCourses(courses);
        when(holidayDao.getByDate(lecture1.getDate())).thenReturn(Optional.empty());
        when(lectureDao.getByTeacherDateAndLectureTime(lecture1.getTeacher().getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByAudienceDateAndLectureTime(lecture1.getAudience().getCapacity(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());
        when(lectureDao.getByGroupDateAndLectureTime(lecture1.getGroups().get(0).getId(), lecture1.getDate(),
                lecture1.getLectureTime().getId())).thenReturn(Optional.empty());

        lectureService.update(lecture1);

        verify(lectureDao).update(lecture1);
    }

    @Test
    void givenLecture_whenDelete_thenDeleteLecture() {
        lectureService.delete(lecture2);

        verify(lectureDao).delete(lecture2);
    }

    @Test
    void givenTeahcer_whenReplaceTeachers_thenReplaceTeachers() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(lecture2);
        List<Course> courses = new ArrayList<>();
        courses.add(course2);
        teacher2.setVacations(new ArrayList<>());
        teacher2.setCourses(courses);
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher2);
        ReassignTeachersDTO reassignData = new ReassignTeachersDTO();
        reassignData.setTeacherId(1);
        reassignData.setTeachers(teachers);
        reassignData.setStart(LocalDate.parse("2021-03-01"));
        reassignData.setEnd(LocalDate.parse("2021-03-30"));
        when(lectureDao.getByTeacherAndDateRange(reassignData.getTeacherId(), reassignData.getStart(),
                reassignData.getEnd())).thenReturn(lectures);

        lectureService.replaceTeachers(reassignData.getTeacherId(), reassignData.getTeachers(), reassignData.getStart(),
                reassignData.getEnd());

        verify(lectureDao).update(lecture2);
    }

    @Test
    void givenWrongTeahcer_whenReplaceTeachers_thenThrowException() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(lecture2);
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        teacher2.setVacations(new ArrayList<>());
        teacher2.setCourses(courses);
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher2);
        ReassignTeachersDTO reassignData = new ReassignTeachersDTO();
        reassignData.setTeacherId(1);
        reassignData.setTeachers(teachers);
        reassignData.setStart(LocalDate.parse("2021-03-01"));
        reassignData.setEnd(LocalDate.parse("2021-03-30"));
        when(lectureDao.getByTeacherAndDateRange(reassignData.getTeacherId(), reassignData.getStart(),
                reassignData.getEnd())).thenReturn(lectures);

        Exception exception = assertThrows(LectureNotReassignException.class,
                () -> lectureService.replaceTeachers(reassignData.getTeacherId(), reassignData.getTeachers(),
                        reassignData.getStart(), reassignData.getEnd()));

        String expected = "Can not reassign lecture with id - 2 to teacher with id - 1";
        String actual = exception.getMessage();
        assertEquals(expected, actual);
    }
    
    interface TestData {

       Lecture lecture1 = new Lecture.Builder()
               .name("testName")
               .audience(audience2)
               .lectureTime(lectureTime2)
               .date(LocalDate.parse("2021-05-20"))
               .course(course2)
               .teacher(teacher1)
               .build();    
       
       Lecture lecture2 = new Lecture.Builder()
               .id(2)
               .name("lecture2")
               .audience(audience2)
               .lectureTime(lectureTime2)
               .date(LocalDate.parse("2021-02-21"))
               .course(course2)
               .teacher(teacher1)
               .build();
       
       Lecture lecture3 = new Lecture.Builder()
               .name("lecture3")
               .audience(audience3)
               .lectureTime(lectureTime2)
               .date(LocalDate.parse("2021-05-23"))
               .course(course2)
               .teacher(teacher1)
               .build(); 
       
       Lecture lecture4 = new Lecture.Builder()
               .name("testName")
               .audience(audience2)
               .lectureTime(lectureTime2)
               .date(LocalDate.parse("2021-05-23"))
               .course(course2)
               .teacher(teacher1)
               .build();  
    }
}

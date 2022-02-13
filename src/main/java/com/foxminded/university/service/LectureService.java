package com.foxminded.university.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

import com.foxminded.university.converter.LectureToLectureDtoConverter;
import com.foxminded.university.dao.HolidayDao;
import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.exceptions.AudienceOccupiedException;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.exceptions.GroupDoesNotLearnLectureCourseException;
import com.foxminded.university.exceptions.GroupOnAnotherLectureException;
import com.foxminded.university.exceptions.LectureNotReassignException;
import com.foxminded.university.exceptions.LectureOnHolidayException;
import com.foxminded.university.exceptions.LectureOnWeekendException;
import com.foxminded.university.exceptions.NotEnoughAudienceCapacityException;
import com.foxminded.university.exceptions.NotQualifiedTeacherException;
import com.foxminded.university.exceptions.ServiceException;
import com.foxminded.university.exceptions.TeacherInVacationException;
import com.foxminded.university.exceptions.NotAvailableTeacherException;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Teacher;

@Service
public class LectureService {

    private static final Logger log = LoggerFactory.getLogger(LectureService.class);
    private LectureDao lectureDao;
    private HolidayDao holidayDao;

    public LectureService(LectureDao lectureDao, HolidayDao holidayDao) {
        this.lectureDao = lectureDao;
        this.holidayDao = holidayDao;
    }

    public void create(Lecture lecture) {
        log.debug("Checking lecture with name - '{}' for validity before creation", lecture.getName());
        validateLecture(lecture);
        lectureDao.create(lecture);
    }

    public Lecture getById(int id) {
        log.debug("Retriving lecture by id - '{}'", id);
        return lectureDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Can't retrieve lecture by id - '%d'", id)));
    }
    
    public List<Lecture> getAll() {
        return lectureDao.getAll();
    }

    public Page<Lecture> getAll(Pageable page) {
        log.debug("Retrieving all lectures");
        return lectureDao.getAll(page);
    }

    public void update(Lecture lecture) {
        log.debug("Checking lecture with name - '{}' for validity before creation", lecture.getName());
        validateLecture(lecture);
        lectureDao.update(lecture);
    }

    public void delete(Lecture lecture) {
        log.debug("Deleting lecture with id - '{}'", lecture.getId());
        lectureDao.delete(lecture);
    }
    
    public List<LectureDto> getByTeacherAndDate(int teacherId, LocalDate date) {
        log.debug("Retrievint teacher lectures for date - {} and convert to LectureDto", date);
        return convertToDto(lectureDao.getByTeacherAndDate(teacherId, date));
    }
    
    public List<LectureDto> getByTeacherAndDateRange(int teacherId, LocalDate startDate, LocalDate endDate) {
        log.debug("Retrieving teacher lectures for range {} -{} and convert to lectureDto", startDate, endDate);
        return convertToDto(lectureDao.getByTeacherAndDateRange(teacherId, startDate, endDate));
    }
    
    public List<LectureDto> getByStudentAndDate(int studentId, LocalDate date) {
        log.debug("Retrieving students lectures for date - {} and convert to LectureDto", date);
        return convertToDto(lectureDao.getByStudentAndDate(studentId, date));
    }
    
    public List<LectureDto> getByStudentAndMonth(int studentId, LocalDate startMonth, LocalDate endMonth) {
        log.debug("Retrieving students lectures for month {} - {} and convert to LectureDto", startMonth, endMonth);
        return convertToDto(lectureDao.getByStudentAndDateRange(studentId, startMonth, endMonth));
    }
    
    @Transactional(rollbackFor = { LectureNotReassignException.class })
    public void replaceTeachers(int currentTeacherId, List<Teacher> teachers, LocalDate start, LocalDate end) {
        log.debug("Reasigning lectures for teacher with id {} from {} to {}", currentTeacherId, start, end);
        if (!teachers.isEmpty()) {
            List<Lecture> lectures = lectureDao.getByTeacherAndDateRange(currentTeacherId, start, end);
            for (Lecture lecture : lectures) {
                setAnotherTeacher(lecture, teachers);
            }
        }
    }
    
    private void setAnotherTeacher(Lecture lecture, List<Teacher> teachers) {
        int teachersIndex = 0;
        while (teachersIndex < teachers.size()) {
            lecture.setTeacher(teachers.get(teachersIndex));
            try {
                validateTeacher(lecture);
                lectureDao.update(lecture);
                break;
            } catch (ServiceException e) {
                log.debug("Teacher with id - {} can't be assigned for lecture - {}", teachers.get(teachersIndex).getId(),
                        lecture.getName());
            }
            
            teachersIndex++;
            if (teachersIndex == teachers.size()) {
                throw new LectureNotReassignException(
                        format("Can not reassign lecture with id - %d to teacher with id - %d", lecture.getId(),
                                lecture.getTeacher().getId()));
            }
        }        
    }
    
    private List<LectureDto> convertToDto(List<Lecture> lectures) {
        return lectures.stream()
                .map(LectureToLectureDtoConverter::toLectureDto)
                .collect(Collectors.toList());
    }

    private void validateLecture(Lecture lecture) {
        verifyHoliday(lecture);
        verifyAudienceCapacity(lecture);
        validateGroups(lecture);
        validateTeacher(lecture);
        veryfyAudienceAvalibility(lecture);
        verifyWeekend(lecture);
    }

    private void verifyHoliday(Lecture lecture) {
        if (holidayDao.getByDate(lecture.getDate()).isPresent()) {
            throw new LectureOnHolidayException(format("Date - '%s' is a holiday", lecture.getDate()));
        }
    }

    private void verifyAudienceCapacity(Lecture lecture) {
        if (lecture.getGroups().stream().map(Group::getStudents)
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .size() > lecture.getAudience().getCapacity()) {
            throw new NotEnoughAudienceCapacityException(
                    format("Audience - '%d' doesn't has inaf capacity", lecture.getAudience().getCapacity()));
        }
    }

    private void validateGroups(Lecture lecture) {
        verifyGroupsShedule(lecture);
        verifyGroupsCourses(lecture);
    }

    private void verifyGroupsCourses(Lecture lecture) {
        if (!lecture.getGroups().stream().map(Group::getCourses).flatMap(List::stream)
                .allMatch(lecture.getCourse()::equals)) {
            throw new GroupDoesNotLearnLectureCourseException(
                    format("One or more groups does't learn course - '%s'", lecture.getCourse().getName()));
        }
    }

    private void verifyGroupsShedule(Lecture lecture) {
        if (lecture.getGroups().stream().map(group -> lectureDao.getByGroupDateAndLectureTime(group.getId(),
                lecture.getDate(), lecture.getLectureTime().getId())).allMatch(Optional::isPresent)) {
            throw new GroupOnAnotherLectureException(format("One or more groups have lectures at - '%s-%s'",
                    lecture.getLectureTime().getStartTime(), lecture.getLectureTime().getEndTime()));
        }

    }

    private void veryfyAudienceAvalibility(Lecture lecture) {
        if (lectureDao.getByAudienceDateAndLectureTime(lecture.getAudience().getId(), lecture.getDate(),
                lecture.getLectureTime().getId()).isPresent()) {
            throw new AudienceOccupiedException(
                    format("Audience - '%s' occupied", lecture.getAudience().getNumber()));
        }
    }

    private void validateTeacher(Lecture lecture) {
        veryfyTeacherVacation(lecture);
        verifyTeacherQualification(lecture);
        verifyTeacherAvalability(lecture);
    }

    private void veryfyTeacherVacation(Lecture lecture) {
        if (!lecture.getTeacher().getVacations().isEmpty() && lecture.getTeacher().getVacations().stream()
                .allMatch(vacation -> vacation.getStart().isBefore(lecture.getDate())
                        && vacation.getEnd().isAfter(lecture.getDate()))) {
            throw new TeacherInVacationException(format("Teacher '%s %s' in vacation",
                    lecture.getTeacher().getFirstName(), lecture.getTeacher().getLastName()));
        }
    }

    private void verifyTeacherAvalability(Lecture lecture) {
        if (lectureDao.getByTeacherDateAndLectureTime(lecture.getTeacher().getId(), lecture.getDate(),
                lecture.getLectureTime().getId()).isPresent()) {
            throw new NotAvailableTeacherException(format("Teacher - '%s %s' on another lecture",
                    lecture.getTeacher().getFirstName(), lecture.getTeacher().getLastName()));
        }
    }

    private void verifyTeacherQualification(Lecture lecture) {
        if (!lecture.getTeacher().getCourses().contains(lecture.getCourse())) {
            throw new NotQualifiedTeacherException(format("Teacher - '%s %s' not qualified for lecture - '%s'",
                    lecture.getTeacher().getFirstName(), lecture.getTeacher().getLastName(), lecture.getName()));
        }
    }

    private void verifyWeekend(Lecture lecture) {
        if (lecture.getDate().getDayOfWeek() == DayOfWeek.SATURDAY
                || lecture.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new LectureOnWeekendException(format("'%s' is a weekend", lecture.getDate()));
        }
    }
}

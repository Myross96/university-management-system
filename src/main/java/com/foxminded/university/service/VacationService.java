package com.foxminded.university.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static java.lang.String.format;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.exceptions.EntityNotFoundException;
import com.foxminded.university.exceptions.IncorrectVacationPeriodException;
import com.foxminded.university.exceptions.TeacherIsBusyException;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;

@Service
public class VacationService {

    private static final Logger log = LoggerFactory.getLogger(VacationService.class);
    private VacationDao vacationDao;
    private TeacherDao teacherDao;
    private LectureDao lectureDao;
    private UniversityConfigProperties properties;
    

    public VacationService(VacationDao vacationDao, TeacherDao teacherDao, LectureDao lectureDao,
            UniversityConfigProperties properties) {
        this.vacationDao = vacationDao;
        this.teacherDao = teacherDao;
        this.lectureDao = lectureDao;
        this.properties = properties;
    }

    public void create(Vacation vacation) {
        log.debug("Checking vacation with period '{} - {}' for validity before creating", vacation.getStart(),
                vacation.getEnd());
        validateVacation(vacation);
        vacationDao.create(vacation);
    }

    public Vacation getById(int id) {
        return vacationDao.getById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Can't retrieve vacation by id - '%d'", id)));
    }

    public List<Vacation> getAll() {
        return vacationDao.getAll();
    }
    
    public Page<Vacation> getAll(Pageable page) {
        return vacationDao.getAll(page);
    }

    public void update(Vacation vacation) {
        log.debug("Checking vacation with period '{} - {}' for validity before updating", vacation.getStart(),
                vacation.getEnd());
        validateVacation(vacation);
        vacationDao.update(vacation);

    }

    public void delete(Vacation vacation) {
        vacationDao.delete(vacation);
    }

    private void validateVacation(Vacation vacation) {
        Teacher teacher = teacherDao.getById(vacation.getTeacherId()).orElseThrow(() -> new EntityNotFoundException(
                format("Can't retrieve reacher by id - '%d'", vacation.getTeacherId())));
        verifyDuration(teacher, vacation);
        verifyTeacherLectures(teacher, vacation.getStart(), vacation.getEnd());
    }

    private void verifyTeacherLectures(Teacher teacher, LocalDate start, LocalDate end) {
        if (!lectureDao.getByTeacherAndDateRange(teacher.getId(), start, end).isEmpty()) {
            throw new TeacherIsBusyException(format("Teacher '%s %s' has lecture for period - '%s-%s'",
                    teacher.getFirstName(), teacher.getLastName(), start, end));
        }
    }

    private void verifyDuration(Teacher teacher, Vacation vacation) {
        if (ChronoUnit.DAYS.between(vacation.getStart(), vacation.getEnd()) > properties.getVacationDays()
                .get(teacher.getAcademicDegree())) {
            throw new IncorrectVacationPeriodException(
                    format("Period '%s - %s' to long", vacation.getStart(), vacation.getEnd()));
        }
    }
}

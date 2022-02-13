package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Lecture;

public interface LectureDao extends GenericDao<Lecture> {

    List<Lecture> getByTeacherAndDate(int teacherId, LocalDate date);

    List<Lecture> getByTeacherAndDateRange(int teacherId, LocalDate startDate, LocalDate endDate);

    List<Lecture> getByStudentAndDate(int studentId, LocalDate date);

    List<Lecture> getByStudentAndDateRange(int studentId, LocalDate startDate, LocalDate endDate);
    
    Optional<Lecture> getByGroupDateAndLectureTime(int groupId, LocalDate date, int lectureTimeId);
    
    Optional<Lecture> getByAudienceDateAndLectureTime(int audienceId, LocalDate date, int lectureTimeId);
    
    Optional<Lecture> getByTeacherDateAndLectureTime(int teacherId, LocalDate date, int lectureTimeId);
}

package com.foxminded.university.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Teacher;

public class ReassignTeachersDTO {

    private int teacherId;
    private LocalDate start;
    private LocalDate end;
    private List<Teacher> teachers = new ArrayList<>();

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}

package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lectures")
@NamedQuery(name = "Lecture.getAll", query = "SELECT l FROM Lecture l FETCH ALL PROPERTIES")
@NamedQuery(name = "Lecture.getAllPagable", query = "SELECT l FROM Lecture l ORDER BY l.id ASC")
@NamedQuery(name = "Lecture.getRecordingsCount", query = "SELECT COUNT(l) FROM Lecture l")
@NamedQuery(name = "Lecture.getByTeacherAndDate", query = "SELECT l FROM Lecture l where l.teacher.id = :id and l.date = :date")
@NamedQuery(name = "Lecture.getByTeacherAndMonth", 
            query = "SELECT l FROM Lecture l FETCH ALL PROPERTIES WHERE "
                    + "l.teacher.id = :id AND l.date BETWEEN :start and :end")
@NamedQuery(name = "Lecture.getByStudentAndDate", 
            query = "SELECT l FROM Lecture l FETCH ALL PROPERTIES LEFT JOIN l.groups AS groups "
                    + "LEFT JOIN groups.students AS student WHERE student.id = :id AND l.date = :date")
@NamedQuery(name = "Lecture.getByStudentAndMonth",
            query = "SELECT l FROM Lecture l FETCH ALL PROPERTIES LEFT JOIN l.groups AS groups "
                    + "LEFT JOIN groups.students AS student WHERE student.id = :id AND l.date BETWEEN :start and :end")
@NamedQuery(name = "Lecture.getByGroupDateAndLectureTime",
            query = "SELECT l FROM Lecture l FETCH ALL PROPERTIES LEFT JOIN l.groups AS group "
                    + "WHERE group.id = :id AND l.date = :date AND l.lectureTime.id = :lectureTime")
@NamedQuery(name = "Lecture.getByAudienceDateAndLectureTime",
            query = "SELECT l FROM Lecture l FETCH ALL PROPERTIES "
                    + "WHERE l.audience.id = :id AND l.date = :date AND l.lectureTime.id = :lectureTime")
@NamedQuery(name = "Lecture.getByTeacherDateAndLectureTime",
            query = "SELECT l FROM Lecture l FETCH ALL PROPERTIES "
                    + "WHERE l.teacher.id = :id AND l.date = :date AND l.lectureTime.id = :lectureTime")
public class Lecture {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;
    private LocalDate date;
    
    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
    
    @OneToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
    
    @ManyToMany
    @JoinTable(name = "lectures_groups",
    joinColumns = @JoinColumn(name = "lecture_id"),
    inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups = new ArrayList<>();
    
    @OneToOne
    @JoinColumn(name = "audience_id", referencedColumnName = "id")
    private Audience audience;
    
    @OneToOne
    @JoinColumn(name = "lecture_time_id", referencedColumnName = "id")
    private LectureTime lectureTime;

    public Lecture() {
    }

    public Lecture(int id, String name, Course course, Teacher teacher, List<Group> groups, LocalDate date,
            Audience audience, LectureTime lectureTime) {
        super();
        this.id = id;
        this.name = name;
        this.course = course;
        this.teacher = teacher;
        this.groups = groups;
        this.date = date;
        this.audience = audience;
        this.lectureTime = lectureTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public LectureTime getLectureTime() {
        return lectureTime;
    }

    public void setLectureTime(LectureTime lectureTime) {
        this.lectureTime = lectureTime;
    }

    public static class Builder {

        private int id;
        private String name;
        private Course course;
        private Teacher teacher;
        private List<Group> groups;
        private LocalDate date;
        private Audience audience;
        private LectureTime lectureTime;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder course(Course course) {
            this.course = course;
            return this;
        }

        public Builder teacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder groups(List<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder audience(Audience audience) {
            this.audience = audience;
            return this;
        }

        public Builder lectureTime(LectureTime lectureTime) {
            this.lectureTime = lectureTime;
            return this;
        }

        public Lecture build() {
            return new Lecture(id, name, course, teacher, groups, date, audience, lectureTime);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Lecture lecture = (Lecture) obj;

        return lecture.getAudience().equals(audience) && lecture.getCourse().equals(course)
                && lecture.getDate().equals(date) && lecture.getId() == id
                && lecture.getLectureTime().equals(lectureTime) && lecture.getName().equals(name)
                && lecture.getTeacher().equals(teacher);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(audience, course, date, id, lectureTime, name, teacher);
    }
}

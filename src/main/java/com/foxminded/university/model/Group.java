package com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
@NamedQuery(name = "Group.getAll", query = "SELECT g FROM Group g FETCH ALL PROPERTIES")
@NamedQuery(name = "Group.getAllPagable", query = "SELECT g FROM Group g ORDER BY g.id ASC")
@NamedQuery(name = "Group.getByName", query = "SELECT g FROM Group g LEFT JOIN FETCH g.students WHERE g.name = :name")
@NamedQuery(name = "Group.getRecordingsCount", query = "SELECT COUNT(g) FROM Group g")
public class Group {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "group")
    private List<Student> students = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "groups_courses", 
    joinColumns = @JoinColumn(name = "group_id"), 
    inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses = new ArrayList<>();

    public Group() {
    }

    public Group(int id, String name, List<Student> students, List<Course> courses) {
        this.id = id;
        this.name = name;
        this.students = students;
        this.courses = courses;
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public static class Builder {

        private int id;
        private String name;
        private List<Student> students;
        private List<Course> courses;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder students(List<Student> students) {
            this.students = students;
            return this;
        }

        public Builder courses(List<Course> courses) {
            this.courses = courses;
            return this;
        }

        public Group build() {
            return new Group(id, name, students, courses);
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

        Group group = (Group) obj;

        return group.getId() == id && group.getName().equals(name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

package com.foxminded.university.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
@NamedQuery(name = "Course.getAll", query = "FROM Course c")
@NamedQuery(name = "Course.getAllPagable", query = "SELECT c FROM Course c order by c.id asc")
@NamedQuery(name = "Course.getRecordingsCount", query = "SELECT COUNT(c) FROM Course c")
@NamedQuery(name = "Course.getByName", query = "SELECT c FROM Course c WHERE c.name = :name")
public class Course {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;
    private String description;

    public Course() {
    }

    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder {

        private int id;
        private String name;
        private String description;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Course build() {
            return new Course(id, name, description);
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
        Course course = (Course) obj;

        return course.getId() == id && course.getName().equals(name) && course.getDescription().equals(description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}

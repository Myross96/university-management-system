package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "vacations")
@NamedQuery(name = "Vacation.getAll", query = "SELECT v FROM Vacation v")
@NamedQuery(name = "Vacation.getAllPagable", query = "SELECT v FROM Vacation v ORDER BY v.id ASC")
@NamedQuery(name = "Vacation.getRecordingsCount", query = "SELECT COUNT(v) FROM Vacation v")
@NamedQuery(name = "Vacation.getByTeacherId", query = "SELECT ELEMENTS(t.vacations) FROM Teacher t WHERE t.id = :id")
public class Vacation {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    
    @Column(name = "start_date")
    private LocalDate start;
    
    @Column(name = "end_date")
    private LocalDate end;
    
    @Column(name = "teacher_id")
    private int teacherId;

    public Vacation() {

    }

    public Vacation(int id, LocalDate start, LocalDate end, int teacherId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.teacherId = teacherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public static class Builder {

        private int id;
        private LocalDate start;
        private LocalDate end;
        private int teacherId;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder start(LocalDate start) {
            this.start = start;
            return this;
        }

        public Builder end(LocalDate end) {
            this.end = end;
            return this;
        }

        public Builder teacherId(int teacherId) {
            this.teacherId = teacherId;
            return this;
        }

        public Vacation build() {
            return new Vacation(id, start, end, teacherId);
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

        Vacation vacation = (Vacation) obj;

        return vacation.getId() == id && vacation.getStart().equals(start) && vacation.getEnd().equals(end);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, start, end);
    }
}

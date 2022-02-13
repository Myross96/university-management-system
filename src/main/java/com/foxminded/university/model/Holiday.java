package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "holidays")
@NamedQuery(name = "Holiday.getAll", query = "SELECT h FROM Holiday h")
@NamedQuery(name = "Holiday.getAllPagable", query = "SELECT h FROM Holiday h ORDER BY h.id ASC")
@NamedQuery(name = "Holiday.getRecordingsCount", query = "SELECT COUNT(h) FROM Holiday h")
@NamedQuery(name = "Holiday.getByDate", query = "SELECT h FROM Holiday h WHERE h.date = :date")
public class Holiday {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;
    private LocalDate date;

    public Holiday() {
    }

    public Holiday(int id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static class Builder {

        private int id;
        private String name;
        private LocalDate date;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Holiday build() {
            return new Holiday(id, name, date);
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

        Holiday holiday = (Holiday) obj;

        return holiday.getId() == id && holiday.getName().equals(name) && holiday.getDate().equals(date);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, date);
    }
}

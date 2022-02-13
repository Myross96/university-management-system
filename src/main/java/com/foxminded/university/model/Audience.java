package com.foxminded.university.model;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "audiences")
@NamedQuery(name = "Audience.getAll", query = "FROM Audience a")
@NamedQuery(name = "Audience.getRecordingsCount", query = "SELECT COUNT(a) FROM Audience a")
@NamedQuery(name = "Audience.getByNamber", query = "SELECT a FROM Audience a WHERE a.number = :number")
@NamedQuery(name = "Audience.getAllPagable", query = "SELECT a FROM Audience a order by a.id asc")
public class Audience {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private int number;
    private int capacity;

    public Audience() {
    }

    public Audience(int id, int number, int capacity) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public static class Builder {

        private int id;
        private int number;
        private int capacity;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder number(int number) {
            this.number = number;
            return this;
        }

        public Builder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Audience build() {
            return new Audience(id, number, capacity);
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

        Audience audience = (Audience) obj;
        return id == audience.getId() && number == audience.getNumber() && capacity == audience.getCapacity();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, number, capacity);
    }
}

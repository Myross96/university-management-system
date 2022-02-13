package com.foxminded.university.model;

import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "lectures_times")
@NamedQuery(name = "LectureTime.getAll", query = "SELECT l FROM LectureTime l")
@NamedQuery(name = "LectureTime.getAllPagable", query = "SELECT l FROM LectureTime l ORDER BY l.id ASC")
@NamedQuery(name = "LectureTime.getRecordingsCount", query = "SELECT COUNT(l) FROM LectureTime l")
public class LectureTime {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "end_time")
    private LocalTime endTime;

    public LectureTime() {
    }

    public LectureTime(int id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public static class Builder {
        
        private int id;
        private LocalTime startTime;
        private LocalTime endTime;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public LectureTime build() {
            return new LectureTime(id, startTime, endTime);
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

        LectureTime lectureTime = (LectureTime) obj;

        return lectureTime.getId() == id && lectureTime.getStartTime().equals(startTime)
                && lectureTime.getEndTime().equals(endTime);
    }
    
    public int hashCode() {
        return Objects.hash(id, startTime, endTime);
    }
}

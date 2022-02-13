package com.foxminded.university.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.foxminded.university.model.Degree;

@ConfigurationProperties(prefix = "university")
public class UniversityConfigProperties {

    private int maxGroupSize;
    private int minLectureDurationInMinutes;
    private Map<Degree, Integer> vacationDays;

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public int getMinLectureDurationInMinutes() {
        return minLectureDurationInMinutes;
    }

    public void setMinLectureDurationInMinutes(int minLectureDurationInMinutes) {
        this.minLectureDurationInMinutes = minLectureDurationInMinutes;
    }

    public Map<Degree, Integer> getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(Map<Degree, Integer> vacationDays) {
        this.vacationDays = vacationDays;
    }
}

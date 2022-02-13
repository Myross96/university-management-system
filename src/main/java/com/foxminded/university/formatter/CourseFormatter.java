package com.foxminded.university.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.Course;

public class CourseFormatter implements Formatter<Course> {

    @Override
    public String print(Course course, Locale locale) {
        return course.toString();
    }

    @Override
    public Course parse(String text, Locale locale) throws ParseException {
        Course course = new Course();
        if (text != null) {
            String[] parts = text.split(",");
            course.setId(Integer.parseInt(parts[0]));
        }
        return course;
    }
}

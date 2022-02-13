package com.foxminded.university.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.Teacher;

public class TeacherFormatter implements Formatter<Teacher> {

    @Override
    public String print(Teacher teacher, Locale locale) {
        return teacher.toString();
    }

    @Override
    public Teacher parse(String text, Locale locale) throws ParseException {
        Teacher teacher = new Teacher();
        if (text != null) {
            String[] parts = text.split(",");
            teacher.setId(Integer.parseInt(parts[0]));
        }
        return teacher;
    }
}

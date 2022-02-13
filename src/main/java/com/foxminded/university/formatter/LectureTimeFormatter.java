package com.foxminded.university.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.LectureTime;

public class LectureTimeFormatter implements Formatter<LectureTime> {

    @Override
    public String print(LectureTime lectureTime, Locale locale) {
        return lectureTime.toString();
    }

    @Override
    public LectureTime parse(String text, Locale locale) throws ParseException {
        LectureTime lectureTime = new LectureTime();
        if (text != null) {
            String[] parts = text.split(",");
            lectureTime.setId(Integer.parseInt(parts[0]));
        }
        return lectureTime;
    }
}

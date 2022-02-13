package com.foxminded.university.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.Audience;

public class AudienceFormatter implements Formatter<Audience> {

    @Override
    public String print(Audience audience, Locale locale) {
        return audience.toString();
    }

    @Override
    public Audience parse(String text, Locale locale) throws ParseException {
        Audience audience = new Audience();
        if (text != null) {
            String[] parts = text.split(",");
            audience.setId(Integer.parseInt(parts[0]));
        }
        return audience;
    }
}

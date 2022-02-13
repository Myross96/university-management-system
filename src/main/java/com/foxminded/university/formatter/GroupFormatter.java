package com.foxminded.university.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.Group;

public class GroupFormatter implements Formatter<Group> {
    
    @Override
    public String print(Group group, Locale locale) {
        return group.toString();
    }

    @Override
    public Group parse(String text, Locale locale) throws ParseException {
        Group group = new Group();
        if (text != null) {
            String[] parts = text.split(",");
            group.setId(Integer.parseInt(parts[0]));
        }
        return group;
    }
}

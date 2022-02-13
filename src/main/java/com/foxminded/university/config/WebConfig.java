package com.foxminded.university.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.foxminded.university.formatter.AudienceFormatter;
import com.foxminded.university.formatter.CourseFormatter;
import com.foxminded.university.formatter.GroupFormatter;
import com.foxminded.university.formatter.LectureTimeFormatter;
import com.foxminded.university.formatter.TeacherFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new CourseFormatter());
        registry.addFormatter(new GroupFormatter());
        registry.addFormatter(new AudienceFormatter());
        registry.addFormatter(new TeacherFormatter());
        registry.addFormatter(new LectureTimeFormatter());
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }
}

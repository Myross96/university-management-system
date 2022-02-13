package com.foxminded.university.converter;

import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.Lecture;

public class LectureToLectureDtoConverter {

    private LectureToLectureDtoConverter() {

    }

    public static LectureDto toLectureDto(Lecture lecture) {
        LectureDto dto = new LectureDto();
        dto.setId(lecture.getId());
        dto.setTitle(lecture.getName());
        dto.setStart(lecture.getDate().toString());
        dto.setEnd(lecture.getDate().toString());
        return dto;
    }
}

package com.foxminded.university.dao;

import java.util.Optional;

import com.foxminded.university.model.Course;

public interface CourseDao extends GenericDao<Course> {

    Optional<Course> getByName(String name);
}

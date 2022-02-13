package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Student;

public interface StudentDao extends GenericDao<Student> {

    List<Student> getByGroupId(int groupId);
}

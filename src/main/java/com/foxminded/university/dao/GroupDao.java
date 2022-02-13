package com.foxminded.university.dao;

import java.util.Optional;

import com.foxminded.university.model.Group;

public interface GroupDao extends GenericDao<Group> {

    Optional<Group> getByName(String name);
}

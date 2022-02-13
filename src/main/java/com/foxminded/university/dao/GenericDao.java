package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericDao<T> {

    void create(T entity);

    Optional<T> getById(int id);

    List<T> getAll();
    
    Page<T> getAll(Pageable page);

    void update(T entity);

    void delete(T entity);

    int count();
}

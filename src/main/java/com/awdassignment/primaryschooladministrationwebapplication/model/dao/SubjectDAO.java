package com.awdassignment.primaryschooladministrationwebapplication.model.dao;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectDAO {
    Long save(Subject subject);

    void update(Subject subject);

    void delete(Long id);

    Optional<Subject> findById(Long id);

    Optional<Subject> findByName(String name);

    List<Subject> findAll();
}


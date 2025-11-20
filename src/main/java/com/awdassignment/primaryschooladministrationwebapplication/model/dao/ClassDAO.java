package com.awdassignment.primaryschooladministrationwebapplication.model.dao;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.SchoolClass;

import java.util.List;
import java.util.Optional;

public interface ClassDAO {
    Long save(SchoolClass schoolClass);

    void update(SchoolClass schoolClass);

    void delete(Long id);

    Optional<SchoolClass> findById(Long id);

    Optional<SchoolClass> findByName(String name);

    List<SchoolClass> findAll();
}


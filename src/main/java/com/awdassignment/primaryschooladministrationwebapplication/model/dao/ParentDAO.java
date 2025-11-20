package com.awdassignment.primaryschooladministrationwebapplication.model.dao;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Parent;

import java.util.List;
import java.util.Optional;

public interface ParentDAO {
    Long save(Parent parent);

    void update(Parent parent);

    Optional<Parent> findById(Long id);

    Optional<Parent> findByUserId(Long userId);

    List<Parent> findAll();
}


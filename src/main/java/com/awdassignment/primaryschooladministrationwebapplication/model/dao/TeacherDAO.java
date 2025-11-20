package com.awdassignment.primaryschooladministrationwebapplication.model.dao;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherDAO {
    Long save(Teacher teacher);

    void update(Teacher teacher);

    void delete(Long id);

    Optional<Teacher> findById(Long id);

    Optional<Teacher> findByOmang(String omangOrPassportNo);

    List<Teacher> findAll();

    List<Teacher> search(String name, String subject);
}


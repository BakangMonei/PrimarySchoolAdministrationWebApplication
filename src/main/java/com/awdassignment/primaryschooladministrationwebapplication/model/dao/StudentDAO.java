package com.awdassignment.primaryschooladministrationwebapplication.model.dao;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    Long save(Student student);

    void update(Student student);

    void delete(Long id);

    Optional<Student> findById(Long id);

    Optional<Student> findByBirthCertificateNo(String birthCertificateNo);

    List<Student> findAll();

    List<Student> search(String name, String surname, String className, String status, LocalDate dob);

    List<Student> findTopStudentsByClass(String className, int limit);
}


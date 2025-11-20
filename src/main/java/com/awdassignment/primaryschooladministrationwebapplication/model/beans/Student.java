package com.awdassignment.primaryschooladministrationwebapplication.model.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Student implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String birthCertificateNo;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String guardianName;
    private String guardianContactNo;
    private String guardianEmail;
    private LocalDate registrationDate;
    private String status; // Active, Inactive, Transferred, Graduated
    private String currentClass;
    private List<String> subjects;
    private Map<String, Double> subjectGrades;
    private Double averageGrade;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Student() {
    }

    public Student(Long id, String name, String surname, String birthCertificateNo, String gender,
                   LocalDate dateOfBirth, String address, String guardianName, String guardianContactNo,
                   String guardianEmail, LocalDate registrationDate, String status, String currentClass,
                   List<String> subjects, Map<String, Double> subjectGrades, Double averageGrade, String notes,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthCertificateNo = birthCertificateNo;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.guardianName = guardianName;
        this.guardianContactNo = guardianContactNo;
        this.guardianEmail = guardianEmail;
        this.registrationDate = registrationDate;
        this.status = status;
        this.currentClass = currentClass;
        this.subjects = subjects;
        this.subjectGrades = subjectGrades;
        this.averageGrade = averageGrade;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void upsertGrade(String subject, Double grade) {
        Objects.requireNonNull(subject, "Subject must not be null");
        Objects.requireNonNull(grade, "Grade must not be null");
        if (subjectGrades == null) {
            subjectGrades = new HashMap<>();
        }
        subjectGrades.put(subject, grade);
        recalculateAverage();
    }

    public void recalculateAverage() {
        if (subjectGrades == null || subjectGrades.isEmpty()) {
            averageGrade = 0d;
            return;
        }
        double sum = subjectGrades.values().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
        averageGrade = Math.round((sum / subjectGrades.size()) * 10.0) / 10.0;
    }

    public Map<String, Double> getSubjectGradesUnmodifiable() {
        if (subjectGrades == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(subjectGrades);
    }

    public String getSubjectsCsv() {
        if (subjects == null || subjects.isEmpty()) {
            return "";
        }
        return String.join(", ", subjects);
    }

    public String getSubjectGradesCsv() {
        if (subjectGrades == null || subjectGrades.isEmpty()) {
            return "";
        }
        return subjectGrades.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(", "));
    }

    // Getters and setters omitted for brevity but generated below

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthCertificateNo() {
        return birthCertificateNo;
    }

    public void setBirthCertificateNo(String birthCertificateNo) {
        this.birthCertificateNo = birthCertificateNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianContactNo() {
        return guardianContactNo;
    }

    public void setGuardianContactNo(String guardianContactNo) {
        this.guardianContactNo = guardianContactNo;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public Map<String, Double> getSubjectGrades() {
        return subjectGrades;
    }

    public void setSubjectGrades(Map<String, Double> subjectGrades) {
        this.subjectGrades = subjectGrades;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}


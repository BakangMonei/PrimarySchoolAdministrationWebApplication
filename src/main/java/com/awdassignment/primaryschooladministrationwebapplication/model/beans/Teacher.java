package com.awdassignment.primaryschooladministrationwebapplication.model.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Teacher implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String omangOrPassportNo;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String contactNo;
    private String email;
    private String qualifications;
    private List<String> subjectsQualifiedToTeach;
    private Map<String, String> classToSubjectMap;
    private LocalDate dateJoined;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Teacher() {
    }

    // getters and setters generated below

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

    public String getOmangOrPassportNo() {
        return omangOrPassportNo;
    }

    public void setOmangOrPassportNo(String omangOrPassportNo) {
        this.omangOrPassportNo = omangOrPassportNo;
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public List<String> getSubjectsQualifiedToTeach() {
        return subjectsQualifiedToTeach;
    }

    public void setSubjectsQualifiedToTeach(List<String> subjectsQualifiedToTeach) {
        this.subjectsQualifiedToTeach = subjectsQualifiedToTeach;
    }

    public Map<String, String> getClassToSubjectMap() {
        return classToSubjectMap;
    }

    public void setClassToSubjectMap(Map<String, String> classToSubjectMap) {
        this.classToSubjectMap = classToSubjectMap;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
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

    public String getSubjectsQualifiedCsv() {
        if (subjectsQualifiedToTeach == null || subjectsQualifiedToTeach.isEmpty()) {
            return "";
        }
        return String.join(", ", subjectsQualifiedToTeach);
    }

    public String getClassSubjectMapCsv() {
        if (classToSubjectMap == null || classToSubjectMap.isEmpty()) {
            return "";
        }
        return classToSubjectMap.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(", "));
    }
}


package com.awdassignment.primaryschooladministrationwebapplication.model.dao;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.User;
import com.awdassignment.primaryschooladministrationwebapplication.model.beans.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Long save(User user);

    void update(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByRememberMeToken(String token);

    List<User> findByRole(UserRole role);
}


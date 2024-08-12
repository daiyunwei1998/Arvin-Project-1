package com.stylish.stylish.repository;

import com.stylish.stylish.model.User;

import java.util.Optional;


public interface UserDAO {
    public void addUser(User user);
    public User getUserById(long id);
    public Optional<User> getUserByEmail(String email);

    User getUserByUserName(String username);

    public boolean existsByEmail(String email);
}

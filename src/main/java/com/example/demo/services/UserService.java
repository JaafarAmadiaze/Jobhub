package com.example.demo.services;


import com.example.demo.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    List<User> getAllUsers();
    User getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);


    List<User> getUsersByRole(String role);
    // "RECRUITER" or "JOB_SEEKER"
    Optional<User> findByEmail(String email); // optional: useful for login
}

package com.sandeep.authentication.service;

import com.sandeep.authentication.entity.User;
import com.sandeep.authentication.repository.UserRepository;
import com.sandeep.authentication.exception.ResourceNotFoundException;
import com.sandeep.authentication.exception.DuplicateResourceException;
import com.sandeep.authentication.util.PasswordEncoderUtil;
import com.sandeep.authentication.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private PasswordEncoderUtil passwordEncoder; // BCryptPasswordEncoder bean

    // -------- Create  normal user creation but hum SignupRequest k object k medium se krege--------
    // public User createUser(User user) {
    //     // Duplicate check
    //     if (userRepository.findByUsername(user.getUsername()).isPresent()) {
    //         throw new DuplicateResourceException("Username already exists: " + user.getUsername());
    //     }
    //     if (userRepository.findByEmail(user.getEmail()).isPresent()) {
    //         throw new DuplicateResourceException("Email already exists: " + user.getEmail());
    //     }

    //     return userRepository.save(user);
    // }




     public String signup(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
             throw new DuplicateResourceException("Email already exists: " + request.getEmail());

        }

         // Password Hash
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // New User
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(hashedPassword)
                .role("USER")   // Default role , isko baad me modify krege/
                .subscription("FREE")
                .build();

        userRepository.save(newUser);
        return "User registered successfully";
    }

    // -------- Read --------
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    // -------- Update --------
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPasswordHash(userDetails.getPasswordHash());
        user.setRole(userDetails.getRole());
        user.setSubscription(userDetails.getSubscription());
        return userRepository.save(user);
    }

    // Change only role (admin use-case)
    public User changeUserRole(Long id, String newRole) {
        User user = getUserById(id);
        user.setRole(newRole);
        return userRepository.save(user);
    }

    // Change subscription
    public User changeSubscription(Long id, String subscription) {
        User user = getUserById(id);
        user.setSubscription(subscription);
        return userRepository.save(user);
    }

    // -------- Delete --------
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // -------- Pagination (Offset based) --------
    public Page<User> getAllUsersOffsetPagination(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    // -------- Pagination (Cursor based) --------

    // isko baad me implement krege
    // public List<User> getUsersAfterId(Long lastId, int size) {
    //     // अगर lastId null है तो पहला page देंगे
    //     if (lastId == null) {
    //         return userRepository.findTopNOrderByIdAsc(size);
    //     } else {
    //         return userRepository.findNextPage(lastId, size);
    //     }
    // }
}

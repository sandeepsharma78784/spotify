
package com.sandeep.authentication.service;

import com.sandeep.authentication.entity.User;
import com.sandeep.authentication.repository.UserRepository;
import com.sandeep.authentication.exception.ResourceNotFoundException;
import com.sandeep.authentication.exception.DuplicateResourceException;
import com.sandeep.authentication.util.*;
import com.sandeep.authentication.dto.*;

import java.util.*;


import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor; //RequiredArgsConstructor

@Service
@RequiredArgsConstructor // no need of autowrired only declare with finala and need lombok dependency

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoder; // BCryptPasswordEncoder bean
    private final JwtUtil jwtUtil; // custom JWT utility


   public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

// Map<String, Object> response = new HashMap<>();
// response.put("token", jwtUtil.generateToken(username));

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("subscription", user.getSubscription());
        return jwtUtil.generateToken(claims,user.getUsername());
        // ye token  controller ki ya dusri ms k controller ki methods use kregi
    }


// iske caller ne map ka use nahi kiya to optional ki jarurat nahi he
     public User createUser(User user) {
        // Duplicate check
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists: " + user.getUsername());
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists: " + user.getEmail());
        }

        return userRepository.save(user);
    }

}

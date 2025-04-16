package com.studenthub.auth.service;

import com.studenthub.auth.dto.AuthResponse;
import com.studenthub.auth.dto.LoginRequest;
import com.studenthub.auth.dto.SignupRequest;
import com.studenthub.auth.dto.RegisterRequest;
import com.studenthub.auth.model.User;
import com.studenthub.auth.model.UserRole;
import com.studenthub.auth.repository.JsonUserRepository;
import com.studenthub.auth.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JsonUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getRole().toString());
    }

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getRole().toString());
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if user exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));

        userRepository.save(user);

        // Generate token and return
        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getRole().toString());
    }
}
package com.studenthub.auth.dto;

import com.studenthub.auth.model.UserRole;
import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private UserRole role;
} 
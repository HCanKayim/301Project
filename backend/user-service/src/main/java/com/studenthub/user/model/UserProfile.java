package com.studenthub.user.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private String studentNumber;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
}

enum UserRole {
    STUDENT,
    TEACHER
} 
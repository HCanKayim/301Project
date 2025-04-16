package com.studenthub.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private UserRole role;

    public User() {
        this.id = java.util.UUID.randomUUID().toString();
    }
}
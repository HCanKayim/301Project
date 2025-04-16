package com.studenthub.auth.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studenthub.auth.model.User;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonUserRepository {
    private final String JSON_FILE_PATH;
    private final ObjectMapper objectMapper;
    private List<User> users;

    public JsonUserRepository() {
        // Get the absolute path to the project root
        String projectRoot = System.getProperty("user.dir");
        this.JSON_FILE_PATH = Paths.get(projectRoot, "users.json").toString();
        System.out.println("Using users.json at: " + JSON_FILE_PATH); // Debug log

        this.objectMapper = new ObjectMapper();
        this.users = loadUsers();
    }

    private List<User> loadUsers() {
        File file = new File(JSON_FILE_PATH);
        try {
            if (!file.exists()) {
                System.out.println("Creating new users.json file at: " + file.getAbsolutePath()); // Debug log
                file.createNewFile();
                objectMapper.writeValue(file, new ArrayList<>());
                return new ArrayList<>();
            }
            System.out.println("Loading existing users from: " + file.getAbsolutePath()); // Debug log
            return objectMapper.readValue(file, new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage()); // Error log
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveUsers() {
        try {
            File file = new File(JSON_FILE_PATH);
            System.out.println("Saving " + users.size() + " users to: " + file.getAbsolutePath()); // Debug log
            objectMapper.writeValue(file, users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage()); // Error log
            e.printStackTrace();
            throw new RuntimeException("Failed to save users: " + e.getMessage(), e);
        }
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Optional<User> findById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User save(User user) {
        Optional<User> existingUser = findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            users.remove(existingUser.get());
        }
        users.add(user);
        saveUsers();
        System.out.println("Saved user: " + user.getUsername()); // Debug log
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }
}
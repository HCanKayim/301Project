package com.studenthub.auth.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studenthub.auth.model.Course;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JsonCourseRepository {
    private final String JSON_FILE_PATH;
    private final ObjectMapper objectMapper;
    private List<Course> courses;

    public JsonCourseRepository() {
        String projectRoot = System.getProperty("user.dir");
        this.JSON_FILE_PATH = Paths.get(projectRoot, "courses.json").toString();
        System.out.println("Using courses.json at: " + JSON_FILE_PATH);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.courses = loadCourses();
    }

    private List<Course> loadCourses() {
        File file = new File(JSON_FILE_PATH);
        try {
            if (!file.exists()) {
                System.out.println("Creating new courses.json file at: " + file.getAbsolutePath());
                file.createNewFile();
                objectMapper.writeValue(file, new ArrayList<>());
                return new ArrayList<>();
            }
            System.out.println("Loading existing courses from: " + file.getAbsolutePath());
            return objectMapper.readValue(file, new TypeReference<List<Course>>() {
            });
        } catch (IOException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveCourses() {
        try {
            File file = new File(JSON_FILE_PATH);
            System.out.println("Saving " + courses.size() + " courses to: " + file.getAbsolutePath());
            objectMapper.writeValue(file, courses);
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save courses: " + e.getMessage(), e);
        }
    }

    public List<Course> findAll() {
        return new ArrayList<>(courses);
    }

    public Optional<Course> findById(String id) {
        return courses.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst();
    }

    public List<Course> findByInstructorId(String instructorId) {
        return courses.stream()
                .filter(course -> course.getInstructorId().equals(instructorId))
                .collect(Collectors.toList());
    }

    public Course save(Course course) {
        if (!course.isValidSchedule()) {
            throw new IllegalArgumentException("Invalid course schedule");
        }

        Optional<Course> existingCourse = findById(course.getId());
        if (existingCourse.isPresent()) {
            courses.remove(existingCourse.get());
        }
        courses.add(course);
        saveCourses();
        return course;
    }

    public void delete(String id) {
        courses.removeIf(course -> course.getId().equals(id));
        saveCourses();
    }
}
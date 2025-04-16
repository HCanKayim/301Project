package com.studenthub.auth.controller;

import com.studenthub.auth.model.Course;
import com.studenthub.auth.repository.JsonCourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {
    private final JsonCourseRepository courseRepository;

    public CourseController(JsonCourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable String instructorId) {
        return ResponseEntity.ok(courseRepository.findByInstructorId(instructorId));
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        // Check for instructor's time conflicts
        List<Course> instructorCourses = courseRepository.findByInstructorId(course.getInstructorId());
        for (Course existingCourse : instructorCourses) {
            if (course.hasTimeConflict(existingCourse)) {
                return ResponseEntity.badRequest()
                        .body("Time conflict with your existing course: " + existingCourse.getTitle());
            }
        }
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<?> enrollStudent(@PathVariable String courseId, @RequestBody Map<String, String> request) {
        String studentId = request.get("studentId");
        if (studentId == null) {
            return ResponseEntity.badRequest().body("Student ID is required");
        }

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        if (course.getEnrolled() >= course.getCapacity()) {
            return ResponseEntity.badRequest().body("Course is full");
        }

        if (course.isStudentEnrolled(studentId)) {
            return ResponseEntity.badRequest().body("Student is already enrolled");
        }

        // Check for student's time conflicts
        List<Course> allCourses = courseRepository.findAll();
        for (Course existingCourse : allCourses) {
            if (existingCourse.isStudentEnrolled(studentId) && course.hasTimeConflict(existingCourse)) {
                return ResponseEntity.badRequest()
                        .body("Time conflict with your enrolled course: " + existingCourse.getTitle());
            }
        }

        course.addStudent(studentId);
        courseRepository.save(course);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/enroll")
    public ResponseEntity<?> unenrollStudent(@PathVariable String courseId, @RequestBody Map<String, String> request) {
        String studentId = request.get("studentId");
        if (studentId == null) {
            return ResponseEntity.badRequest().body("Student ID is required");
        }

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        if (!course.isStudentEnrolled(studentId)) {
            return ResponseEntity.badRequest().body("Student is not enrolled in this course");
        }

        course.removeStudent(studentId);
        courseRepository.save(course);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable String id, @RequestBody Course course) {
        if (!courseRepository.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        if (!courseRepository.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.delete(id);
        return ResponseEntity.ok().build();
    }
}
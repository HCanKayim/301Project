package com.studenthub.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    private String id;
    private String title;
    private String description;
    private String instructorId;
    private String code;
    private int capacity;
    private List<String> enrolledStudents;
    private int enrolled;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    public Course() {
        this.id = java.util.UUID.randomUUID().toString();
        this.enrolledStudents = new ArrayList<>();
        this.enrolled = 0;
    }

    public void addStudent(String studentId) {
        if (!enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            this.enrolled = enrolledStudents.size();
        }
    }

    public void removeStudent(String studentId) {
        enrolledStudents.remove(studentId);
        this.enrolled = enrolledStudents.size();
    }

    public boolean isStudentEnrolled(String studentId) {
        return enrolledStudents.contains(studentId);
    }

    public boolean isValidSchedule() {
        if (day == null || startTime == null || endTime == null) {
            return false;
        }

        // Check if day is a weekday (Monday to Friday)
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return false;
        }

        // Define valid time range (9 AM to 8 PM)
        LocalTime earliestTime = LocalTime.of(9, 0);
        LocalTime latestTime = LocalTime.of(20, 0);

        // Check if start time is before end time and within valid range
        return !startTime.isBefore(earliestTime) &&
                !endTime.isAfter(latestTime) &&
                startTime.isBefore(endTime);
    }

    public boolean hasTimeConflict(Course other) {
        if (this.day != other.day) {
            return false;
        }

        // Check if one course's time range overlaps with the other
        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime));
    }
}
package com.studenthub.user.repository;

import com.studenthub.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByEmail(String email);
    UserProfile findByStudentNumber(String studentNumber);
} 
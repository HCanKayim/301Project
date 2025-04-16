package com.studenthub.user.service;

import com.studenthub.user.model.UserProfile;
import com.studenthub.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;
    
    public UserProfile getUserProfile(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
    }
    
    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }
    
    public UserProfile createUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
    
    public UserProfile updateUserProfile(Long id, UserProfile userProfile) {
        UserProfile existingProfile = getUserProfile(id);
        
        existingProfile.setFirstName(userProfile.getFirstName());
        existingProfile.setLastName(userProfile.getLastName());
        existingProfile.setEmail(userProfile.getEmail());
        existingProfile.setPhoneNumber(userProfile.getPhoneNumber());
        existingProfile.setDepartment(userProfile.getDepartment());
        
        return userProfileRepository.save(existingProfile);
    }
    
    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }
} 
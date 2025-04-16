package com.studenthub.announcement.service;

import com.studenthub.announcement.model.Announcement;
import com.studenthub.announcement.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    
    private final AnnouncementRepository announcementRepository;
    
    public Announcement createAnnouncement(Announcement announcement) {
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setUpdatedAt(LocalDateTime.now());
        return announcementRepository.save(announcement);
    }
    
    public Announcement getAnnouncement(String id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));
    }
    
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }
    
    public List<Announcement> getAnnouncementsByAuthor(Long authorId) {
        return announcementRepository.findByAuthorId(authorId);
    }
    
    public List<Announcement> getAnnouncementsByTargetAudience(String targetAudience) {
        return announcementRepository.findByTargetAudience(targetAudience);
    }
    
    public List<Announcement> searchAnnouncements(String title) {
        return announcementRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public Announcement updateAnnouncement(String id, Announcement announcement) {
        Announcement existingAnnouncement = getAnnouncement(id);
        
        existingAnnouncement.setTitle(announcement.getTitle());
        existingAnnouncement.setContent(announcement.getContent());
        existingAnnouncement.setType(announcement.getType());
        existingAnnouncement.setTargetAudience(announcement.getTargetAudience());
        existingAnnouncement.setUpdatedAt(LocalDateTime.now());
        
        return announcementRepository.save(existingAnnouncement);
    }
    
    public void deleteAnnouncement(String id) {
        announcementRepository.deleteById(id);
    }
} 
package com.studenthub.announcement.controller;

import com.studenthub.announcement.model.Announcement;
import com.studenthub.announcement.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {
    
    private final AnnouncementService announcementService;
    
    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        return ResponseEntity.ok(announcementService.createAnnouncement(announcement));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncement(@PathVariable String id) {
        return ResponseEntity.ok(announcementService.getAnnouncement(id));
    }
    
    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }
    
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Announcement>> getAnnouncementsByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(announcementService.getAnnouncementsByAuthor(authorId));
    }
    
    @GetMapping("/target/{targetAudience}")
    public ResponseEntity<List<Announcement>> getAnnouncementsByTargetAudience(
            @PathVariable String targetAudience) {
        return ResponseEntity.ok(announcementService.getAnnouncementsByTargetAudience(targetAudience));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Announcement>> searchAnnouncements(@RequestParam String title) {
        return ResponseEntity.ok(announcementService.searchAnnouncements(title));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(
            @PathVariable String id,
            @RequestBody Announcement announcement) {
        return ResponseEntity.ok(announcementService.updateAnnouncement(id, announcement));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable String id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok().build();
    }
} 
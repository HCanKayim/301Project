package com.studenthub.auth.controller;

import com.studenthub.auth.model.Announcement;
import com.studenthub.auth.repository.JsonAnnouncementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "*")
public class AnnouncementController {
    private final JsonAnnouncementRepository announcementRepository;

    public AnnouncementController(JsonAnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable String id) {
        return announcementRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        return ResponseEntity.ok(announcementRepository.save(announcement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable String id,
            @RequestBody Announcement announcement) {
        if (!announcementRepository.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        announcement.setId(id);
        return ResponseEntity.ok(announcementRepository.save(announcement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable String id) {
        if (!announcementRepository.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        announcementRepository.delete(id);
        return ResponseEntity.ok().build();
    }
}
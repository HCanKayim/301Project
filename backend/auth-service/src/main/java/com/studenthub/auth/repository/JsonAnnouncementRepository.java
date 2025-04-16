package com.studenthub.auth.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studenthub.auth.model.Announcement;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonAnnouncementRepository {
    private final String JSON_FILE_PATH;
    private final ObjectMapper objectMapper;
    private List<Announcement> announcements;

    public JsonAnnouncementRepository() {
        String projectRoot = System.getProperty("user.dir");
        this.JSON_FILE_PATH = Paths.get(projectRoot, "announcements.json").toString();
        System.out.println("Using announcements.json at: " + JSON_FILE_PATH);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.announcements = loadAnnouncements();
    }

    private List<Announcement> loadAnnouncements() {
        File file = new File(JSON_FILE_PATH);
        try {
            if (!file.exists()) {
                System.out.println("Creating new announcements.json file at: " + file.getAbsolutePath());
                file.createNewFile();
                objectMapper.writeValue(file, new ArrayList<>());
                return new ArrayList<>();
            }
            System.out.println("Loading existing announcements from: " + file.getAbsolutePath());
            return objectMapper.readValue(file, new TypeReference<List<Announcement>>() {
            });
        } catch (IOException e) {
            System.err.println("Error loading announcements: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveAnnouncements() {
        try {
            File file = new File(JSON_FILE_PATH);
            System.out.println("Saving " + announcements.size() + " announcements to: " + file.getAbsolutePath());
            objectMapper.writeValue(file, announcements);
        } catch (IOException e) {
            System.err.println("Error saving announcements: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save announcements: " + e.getMessage(), e);
        }
    }

    public List<Announcement> findAll() {
        return new ArrayList<>(announcements);
    }

    public Optional<Announcement> findById(String id) {
        return announcements.stream()
                .filter(announcement -> announcement.getId().equals(id))
                .findFirst();
    }

    public Announcement save(Announcement announcement) {
        Optional<Announcement> existingAnnouncement = findById(announcement.getId());
        if (existingAnnouncement.isPresent()) {
            announcements.remove(existingAnnouncement.get());
        }
        announcements.add(announcement);
        saveAnnouncements();
        System.out.println("Saved announcement: " + announcement.getTitle());
        return announcement;
    }

    public void delete(String id) {
        announcements.removeIf(announcement -> announcement.getId().equals(id));
        saveAnnouncements();
    }
}
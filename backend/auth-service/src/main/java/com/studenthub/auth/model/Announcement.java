package com.studenthub.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Announcement {
    private String id;
    private String title;
    private String content;
    private String authorId;
    private String authorName;
    private LocalDateTime createdAt;

    public Announcement() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }
}
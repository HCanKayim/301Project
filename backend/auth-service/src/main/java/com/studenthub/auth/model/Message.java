package com.studenthub.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private String id;
    private String content;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private LocalDateTime createdAt;
    private boolean read;

    public Message() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.read = false;
    }
}
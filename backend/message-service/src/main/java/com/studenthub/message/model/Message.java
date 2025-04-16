package com.studenthub.message.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private MessageStatus status;
} 
package com.studenthub.auth.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.studenthub.auth.model.Message;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JsonMessageRepository {
    private final String JSON_FILE_PATH;
    private final ObjectMapper objectMapper;
    private List<Message> messages;

    public JsonMessageRepository() {
        String projectRoot = System.getProperty("user.dir");
        this.JSON_FILE_PATH = Paths.get(projectRoot, "messages.json").toString();
        System.out.println("Using messages.json at: " + JSON_FILE_PATH);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.messages = loadMessages();
    }

    private List<Message> loadMessages() {
        File file = new File(JSON_FILE_PATH);
        try {
            if (!file.exists()) {
                System.out.println("Creating new messages.json file at: " + file.getAbsolutePath());
                file.createNewFile();
                objectMapper.writeValue(file, new ArrayList<>());
                return new ArrayList<>();
            }
            System.out.println("Loading existing messages from: " + file.getAbsolutePath());
            return objectMapper.readValue(file, new TypeReference<List<Message>>() {
            });
        } catch (IOException e) {
            System.err.println("Error loading messages: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveMessages() {
        try {
            File file = new File(JSON_FILE_PATH);
            System.out.println("Saving " + messages.size() + " messages to: " + file.getAbsolutePath());
            objectMapper.writeValue(file, messages);
        } catch (IOException e) {
            System.err.println("Error saving messages: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save messages: " + e.getMessage(), e);
        }
    }

    public List<Message> findAll() {
        return new ArrayList<>(messages);
    }

    public Optional<Message> findById(String id) {
        return messages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst();
    }

    public List<Message> findBySenderId(String senderId) {
        return messages.stream()
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    public List<Message> findByReceiverId(String receiverId) {
        return messages.stream()
                .filter(message -> message.getReceiverId().equals(receiverId))
                .collect(Collectors.toList());
    }

    public List<Message> findConversation(String userId1, String userId2) {
        return messages.stream()
                .filter(message -> (message.getSenderId().equals(userId1) && message.getReceiverId().equals(userId2)) ||
                        (message.getSenderId().equals(userId2) && message.getReceiverId().equals(userId1)))
                .collect(Collectors.toList());
    }

    public Message save(Message message) {
        Optional<Message> existingMessage = findById(message.getId());
        if (existingMessage.isPresent()) {
            messages.remove(existingMessage.get());
        }
        messages.add(message);
        saveMessages();
        System.out.println("Saved message from " + message.getSenderName() + " to " + message.getReceiverName());
        return message;
    }

    public void delete(String id) {
        messages.removeIf(message -> message.getId().equals(id));
        saveMessages();
    }

    public void markAsRead(String messageId) {
        findById(messageId).ifPresent(message -> {
            message.setRead(true);
            save(message);
        });
    }
}
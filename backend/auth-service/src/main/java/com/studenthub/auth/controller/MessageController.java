package com.studenthub.auth.controller;

import com.studenthub.auth.model.Message;
import com.studenthub.auth.repository.JsonMessageRepository;
import com.studenthub.auth.repository.JsonUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {
    private final JsonMessageRepository messageRepository;
    private final JsonUserRepository userRepository;

    public MessageController(JsonMessageRepository messageRepository, JsonUserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getConversation(
            @PathVariable String userId1,
            @PathVariable String userId2) {
        List<Message> messages = messageRepository.findConversation(userId1, userId2);
        messages.forEach(message -> messageRepository.markAsRead(message.getId()));
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Message>> getUserMessages(@PathVariable String userId) {
        List<Message> received = messageRepository.findByReceiverId(userId);
        List<Message> sent = messageRepository.findBySenderId(userId);
        received.addAll(sent);
        return ResponseEntity.ok(received);
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (message.getSenderId() == null || message.getReceiverId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Verify both users exist
        boolean senderExists = userRepository.findById(message.getSenderId()).isPresent();
        boolean receiverExists = userRepository.findById(message.getReceiverId()).isPresent();

        if (!senderExists || !receiverExists) {
            return ResponseEntity.badRequest().build();
        }

        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.ok(savedMessage);
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable String messageId) {
        messageRepository.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
        messageRepository.delete(messageId);
        return ResponseEntity.ok().build();
    }
}
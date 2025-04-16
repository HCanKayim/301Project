package com.studenthub.message.service;

import com.studenthub.message.model.Message;
import com.studenthub.message.model.MessageStatus;
import com.studenthub.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    
    private final MessageRepository messageRepository;
    
    public Message sendMessage(Message message) {
        message.setSentAt(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);
        return messageRepository.save(message);
    }
    
    public Message getMessage(String id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }
    
    public List<Message> getSentMessages(Long senderId) {
        return messageRepository.findBySenderIdOrderBySentAtDesc(senderId);
    }
    
    public List<Message> getReceivedMessages(Long receiverId) {
        return messageRepository.findByReceiverIdOrderBySentAtDesc(receiverId);
    }
    
    public List<Message> getConversation(Long userId1, Long userId2) {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderBySentAtDesc(userId1, userId2);
        messages.addAll(messageRepository.findBySenderIdAndReceiverIdOrderBySentAtDesc(userId2, userId1));
        messages.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));
        return messages;
    }
    
    public Message markAsRead(String messageId) {
        Message message = getMessage(messageId);
        message.setReadAt(LocalDateTime.now());
        message.setStatus(MessageStatus.READ);
        return messageRepository.save(message);
    }
    
    public void deleteMessage(String id) {
        messageRepository.deleteById(id);
    }
} 
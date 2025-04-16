package com.studenthub.message.repository;

import com.studenthub.message.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderIdOrderBySentAtDesc(Long senderId);
    List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);
    List<Message> findBySenderIdAndReceiverIdOrderBySentAtDesc(Long senderId, Long receiverId);
} 
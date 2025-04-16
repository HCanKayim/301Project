package com.studenthub.announcement.repository;

import com.studenthub.announcement.model.Announcement;
import com.studenthub.announcement.model.AnnouncementType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends MongoRepository<Announcement, String> {
    List<Announcement> findByAuthorId(Long authorId);

    List<Announcement> findByTargetAudience(String targetAudience);

    List<Announcement> findByType(AnnouncementType type);

    List<Announcement> findByTitleContainingIgnoreCase(String title);
}
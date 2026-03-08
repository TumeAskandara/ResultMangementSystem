package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends MongoRepository<Announcement, String> {
    Page<Announcement> findByType(Announcement.AnnouncementType type, Pageable pageable);
    Page<Announcement> findByTargetAudience(Announcement.TargetAudience targetAudience, Pageable pageable);
    Page<Announcement> findByIsActiveAndPublishDateBeforeAndExpiryDateAfter(boolean isActive, LocalDateTime publishDate, LocalDateTime expiryDate, Pageable pageable);
    Page<Announcement> findByIsActive(boolean isActive, Pageable pageable);
    List<Announcement> findByDepartmentId(String departmentId);
    List<Announcement> findByIsPinnedAndIsActive(boolean isPinned, boolean isActive);
}

package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.CalendarEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarEventRepository extends MongoRepository<CalendarEvent, String> {

    @Query("{'$or': [{'departmentId': ?0}, {'targetDepartments': ?0}, {'departmentId': null}]}")
    List<CalendarEvent> findByDepartment(String departmentId);

    @Query("{'startDateTime': {'$gte': ?0, '$lte': ?1}, '$or': [{'departmentId': ?2}, {'targetDepartments': ?2}, {'departmentId': null}]}")
    List<CalendarEvent> findByDateRangeAndDepartment(LocalDateTime startDate, LocalDateTime endDate, String departmentId);

    List<CalendarEvent> findByStartDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{'notificationSent': false, 'startDateTime': {'$lte': ?0}}")
    List<CalendarEvent> findEventsNeedingNotification(LocalDateTime notificationTime);

    List<CalendarEvent> findByCreatedBy(String adminId);

    List<CalendarEvent> findByEventType(CalendarEvent.EventType eventType);
}
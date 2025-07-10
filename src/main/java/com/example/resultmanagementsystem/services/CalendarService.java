package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.CalendarEventRepository;
import com.example.resultmanagementsystem.Dto.Repository.DepartmentRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.model.CalendarEvent;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CalendarService {
    private final CalendarEventRepository calendarEventRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final NotificationLogService notificationLogService;

    public CalendarEvent createEvent(CalendarEvent event) {
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        CalendarEvent savedEvent = calendarEventRepository.save(event);


        // Notify students if the event should be notified immediately
        if (shouldNotifyImmediately(savedEvent)) {
            notifyStudentsAboutEvent(savedEvent);


        }
        return savedEvent;
    }

    public CalendarEvent updateEvent(String eventId, CalendarEvent updatedEvent){
        Optional<CalendarEvent> existingEvent = calendarEventRepository.findById(eventId);
        if(existingEvent.isPresent()){
            CalendarEvent event = existingEvent.get();
            event.setTitle(updatedEvent.getTitle());
            event.setDescription(updatedEvent.getDescription());
            event.setStartDateTime(updatedEvent.getStartDateTime());
            event.setEndDateTime(updatedEvent.getEndDateTime());
            event.setTargetDepartments(updatedEvent.getTargetDepartments());
            event.setEventType(updatedEvent.getEventType());
            event.setLocation(updatedEvent.getLocation());
            event.setStatus(updatedEvent.getStatus());
            event.setUpdatedAt(LocalDateTime.now());

            CalendarEvent saved = calendarEventRepository.save(event);

            // Notify about changes
            notifyStudentsAboutEventUpdate(saved);
            return saved;
        }
        throw new RuntimeException("Event not found with id: " + eventId);
    }


    public void deleteEvent(String eventId) {
        Optional<CalendarEvent> event = calendarEventRepository.findById(eventId);
        if (event.isPresent()) {
            // Notify about cancellation
            notifyStudentsAboutEventCancellation(event.get());
            calendarEventRepository.deleteById(eventId);
        } else {
            throw new RuntimeException("Event not found with id: " + eventId);
        }
    }

    public List<CalendarEvent> getEventsByDepartment(String departmentId) {
        return calendarEventRepository.findByDepartment(departmentId);
    }

    public List<CalendarEvent> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate, String departmentId) {
        if (departmentId != null) {
            return calendarEventRepository.findByDateRangeAndDepartment(startDate, endDate, departmentId);
        }
        return calendarEventRepository.findByStartDateTimeBetween(startDate, endDate);
    }

    public List<CalendarEvent> getStudentEvents(String studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            return getEventsByDepartment(student.get().getDepartmentId());
        }
        throw new RuntimeException("Student not found with id: " + studentId);
    }

    public void notifyStudentsAboutEvent(CalendarEvent event) {
        List<Student> targetStudents = getTargetStudents(event);

        String message = createNotificationMessage(event, "New Event");

        List<String> studentIds = targetStudents.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toList());

        notificationLogService.sendNotification(studentIds, message, event.getEventId());

        // Mark event as notified
        event.setNotificationSent(true);
        calendarEventRepository.save(event);
    }

    public void notifyStudentsAboutEventUpdate(CalendarEvent event) {
        List<Student> targetStudents = getTargetStudents(event);

        String message = createNotificationMessage(event, "Event Updated");

        List<String> studentIds = targetStudents.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toList());

        notificationLogService.sendNotification(studentIds, message, event.getEventId());
    }

    public void notifyStudentsAboutEventCancellation(CalendarEvent event) {
        List<Student> targetStudents = getTargetStudents(event);

        String message = createNotificationMessage(event, "Event Cancelled");

        List<String> studentIds = targetStudents.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toList());

        notificationLogService.sendNotification(studentIds, message, event.getEventId());
    }

    public void processScheduledNotifications() {
        // Find events that need notification (e.g., 24 hours before)
        LocalDateTime notificationTime = LocalDateTime.now().plusHours(24);
        List<CalendarEvent> eventsNeedingNotification =
                calendarEventRepository.findEventsNeedingNotification(notificationTime);

        for (CalendarEvent event : eventsNeedingNotification) {
            notifyStudentsAboutEvent(event);
        }
    }

    private List<Student> getTargetStudents(CalendarEvent event) {
        if (event.getDepartmentId() != null) {
            // Event for specific department
            return studentRepository.findByDepartmentId(event.getDepartmentId());
        } else if (event.getTargetDepartments() != null && !event.getTargetDepartments().isEmpty()) {
            // Event for multiple specific departments
            return studentRepository.findByDepartmentIdIn(event.getTargetDepartments());
        } else {
            // Event for all students
            return studentRepository.findAll();
        }
    }

    private String createNotificationMessage(CalendarEvent event, String action) {
        return String.format("%s: %s\n" +
                        "Date: %s\n" +
                        "Time: %s - %s\n" +
                        "Location: %s\n" +
                        "Description: %s",
                action,
                event.getTitle(),
                event.getStartDateTime().toLocalDate(),
                event.getStartDateTime().toLocalTime(),
                event.getEndDateTime().toLocalTime(),
                event.getLocation() != null ? event.getLocation() : "TBA",
                event.getDescription() != null ? event.getDescription() : "");
    }

    private boolean shouldNotifyImmediately(CalendarEvent event) {
        // Notify immediately if event is within next 7 days
        return event.getStartDateTime().isBefore(LocalDateTime.now().plusDays(7));
    }
}




package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.GlobalExceptionHandler;
import com.example.resultmanagementsystem.model.Notification;
import com.example.resultmanagementsystem.model.Timetable;
import com.example.resultmanagementsystem.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/teacher")
    public ResponseEntity<List<Notification>> getTeacherNotifications(@RequestParam String teacherId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsForTeacher(teacherId);
            return ResponseEntity.ok(notifications);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Notification>> getNotificationsForStudent(@PathVariable String studentId) {
        List<Notification> notifications = notificationService.getNotificationsForStudent(studentId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/exam")
    public ResponseEntity<String> createExamNotification(
            @RequestParam String courseId,
            @RequestParam String examTitle,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime examDate,
            @RequestParam String venue) {

        notificationService.createExamNotification(courseId, examTitle, examDate, venue);
        return ResponseEntity.ok("Exam notification sent to students");
    }


    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable String id) {
        Optional<Notification> notification = notificationService.getNotificationById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<Notification>> getNotificationsByRecipient(@PathVariable String recipientId) {
        List<Notification> notifications = notificationService.getNotificationsByRecipient(recipientId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/recipient/{recipientId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable String recipientId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(recipientId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(createdNotification);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable String id) {
        try {
            Notification notification = notificationService.markAsRead(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // NEW ENDPOINTS FOR SCHEDULE-RELATED NOTIFICATIONS

    /**
     * Create a schedule change notification
     * POST /api/notifications/schedule-change
     */
    @PostMapping("/schedule-change")
    public ResponseEntity<String> createScheduleChangeNotification(
            @RequestBody Timetable timetable,
            @RequestParam String changeType) {
        try {
            notificationService.createScheduleChangeNotification(timetable, changeType);
            return ResponseEntity.ok("Schedule change notification created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create schedule change notification: " + e.getMessage());
        }
    }

    /**
     * Create a substitution notification
     * POST /api/notifications/substitution
     */
    @PostMapping("/substitution")
    public ResponseEntity<String> createSubstitutionNotification(
            @RequestBody Timetable timetable,
            @RequestParam String substituteTeacherId,
            @RequestParam String reason) {
        try {
            notificationService.createSubstitutionNotification(timetable, substituteTeacherId, reason);
            return ResponseEntity.ok("Substitution notification created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create substitution notification: " + e.getMessage());
        }
    }

    /**
     * Manually trigger daily reminders (for testing purposes)
     * POST /api/notifications/daily-reminders/trigger
     */
    @PostMapping("/daily-reminders/trigger")
    public ResponseEntity<String> triggerDailyReminders() {
        try {
            notificationService.sendDailyReminders();
            return ResponseEntity.ok("Daily reminders sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send daily reminders: " + e.getMessage());
        }
    }

    /**
     * Manually trigger processing of pending notifications (for testing purposes)
     * POST /api/notifications/pending/process
     */
    @PostMapping("/pending/process")
    public ResponseEntity<String> processPendingNotifications() {
        try {
            notificationService.processPendingNotifications();
            return ResponseEntity.ok("Pending notifications processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process pending notifications: " + e.getMessage());
        }
    }

    /**
     * Resend a specific notification
     * POST /api/notifications/{id}/resend
     */
    @PostMapping("/{id}/resend")
    public ResponseEntity<String> resendNotification(@PathVariable String id) {
        try {
            Optional<Notification> notificationOpt = notificationService.getNotificationById(id);
            if (notificationOpt.isPresent()) {
                notificationService.sendNotificationAsync(notificationOpt.get());
                return ResponseEntity.ok("Notification resent successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to resend notification: " + e.getMessage());
        }
    }

    // ADDITIONAL UTILITY ENDPOINTS

    /**
     * Get count of unread notifications for a recipient
     * GET /api/notifications/recipient/{recipientId}/unread/count
     */
    @GetMapping("/recipient/{recipientId}/unread/count")
    public ResponseEntity<Long> getUnreadNotificationCount(@PathVariable String recipientId) {
        List<Notification> unreadNotifications = notificationService.getUnreadNotifications(recipientId);
        return ResponseEntity.ok((long) unreadNotifications.size());
    }

    /**
     * Mark all notifications as read for a recipient
     * PATCH /api/notifications/recipient/{recipientId}/read-all
     */
    @PatchMapping("/recipient/{recipientId}/read-all")
    public ResponseEntity<String> markAllAsRead(@PathVariable String recipientId) {
        try {
            List<Notification> unreadNotifications = notificationService.getUnreadNotifications(recipientId);
            for (Notification notification : unreadNotifications) {
                notificationService.markAsRead(notification.getNotificationId());
            }
            return ResponseEntity.ok("All notifications marked as read");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to mark all notifications as read: " + e.getMessage());
        }
    }

    /**
     * Get notifications by type
     * GET /api/notifications/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByType(@PathVariable String type) {
        // Note: You'll need to add this method to your NotificationService
        // List<Notification> notifications = notificationService.getNotificationsByType(type);
        // return ResponseEntity.ok(notifications);
        return ResponseEntity.badRequest().body(null); // Placeholder - implement in service first
    }

    /**
     * Get notifications by priority
     * GET /api/notifications/priority/{priority}
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Notification>> getNotificationsByPriority(@PathVariable String priority) {
        // Note: You'll need to add this method to your NotificationService
        // List<Notification> notifications = notificationService.getNotificationsByPriority(priority);
        // return ResponseEntity.ok(notifications);
        return ResponseEntity.badRequest().body(null); // Placeholder - implement in service first
    }
}
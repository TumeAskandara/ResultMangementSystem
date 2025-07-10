package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.*;
import com.example.resultmanagementsystem.model.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TimetableRepository timetableRepository;
    private final EmailService emailService; // Assume this exists
    private final DepartmentRepository departmentRepository; // Assume this exists
    private final TeacherRepository teacherRepository; // Assume this exists
    private final StudentRepository studentRepository; // Assume this exists

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(String notificationId) {
        return notificationRepository.findById(notificationId);
    }

    public List<Notification> getNotificationsByRecipient(String recipientId) {
        return notificationRepository.findByRecipientIdsContaining(recipientId);
    }

    public List<Notification> getUnreadNotifications(String recipientId) {
        return notificationRepository.findUnreadNotificationsByRecipient(recipientId);
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification markAsRead(String notificationId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    notification.setIsRead(true);
                    return notificationRepository.save(notification);
                })
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    // Create schedule change notification
    public void createScheduleChangeNotification(Timetable timetable, String changeType) {
        Department department = departmentRepository.findById(timetable.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Get all department teachers
        List<Teacher> departmentTeachers = teacherRepository.findByDepartmentId(timetable.getDepartmentId());
        List<String> teacherIds = departmentTeachers.stream()
                .map(Teacher::getTeacherId)
                .collect(Collectors.toList());

        // Get all students in the department
        List<Student> departmentStudents = studentRepository.findByDepartmentId(timetable.getDepartmentId());
        List<String> studentIds = departmentStudents.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toList());

        // CREATE TEACHER NOTIFICATION
        Notification teacherNotification = new Notification();
        teacherNotification.setTitle("Schedule Change Alert - " + department.getDepartmentName());
        teacherNotification.setMessage(String.format(
                "Schedule change for %s department on %s from %s to %s. Type: %s. Please inform your students.",
                department.getDepartmentName(),
                timetable.getDayOfWeek(),
                timetable.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                timetable.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                changeType
        ));
        teacherNotification.setType("SCHEDULE_CHANGE");
        teacherNotification.setPriority("HIGH");
        teacherNotification.setRecipientIds(teacherIds);
        teacherNotification.setRecipientType("TEACHER");
        teacherNotification.setDepartmentId(timetable.getDepartmentId());
        teacherNotification.setTimetableId(timetable.getTimetableId());
        teacherNotification.setScheduledDate(LocalDateTime.now());

        // CREATE STUDENT NOTIFICATION
        Notification studentNotification = new Notification();
        studentNotification.setTitle("Class Schedule Change - " + department.getDepartmentName());
        studentNotification.setMessage(String.format(
                "IMPORTANT: Your %s department class schedule has changed.\n\n" +
                        "New Schedule: %s at %s to %s\n" +
                        "Room: %s\n" +
                        "Change Type: %s\n\n" +
                        "Please update your calendar and arrive on time.",
                department.getDepartmentName(),
                timetable.getDayOfWeek(),
                timetable.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                timetable.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                timetable.getRoomNumber(),
                changeType
        ));
        studentNotification.setType("SCHEDULE_CHANGE");
        studentNotification.setPriority("HIGH");
        studentNotification.setRecipientIds(studentIds);
        studentNotification.setRecipientType("STUDENT");
        studentNotification.setDepartmentId(timetable.getDepartmentId());
        studentNotification.setTimetableId(timetable.getTimetableId());
        studentNotification.setScheduledDate(LocalDateTime.now());

        // Save and send both notifications
        notificationRepository.save(teacherNotification);
        notificationRepository.save(studentNotification);

        sendNotificationAsync(teacherNotification);
        sendNotificationAsync(studentNotification);
    }

    // Create substitution notification
    public void createSubstitutionNotification(Timetable timetable, String substituteTeacherId, String reason) {
        Department department = departmentRepository.findById(timetable.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Teacher originalTeacher = teacherRepository.findById(timetable.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Original teacher not found"));
        Teacher substituteTeacher = teacherRepository.findById(substituteTeacherId)
                .orElseThrow(() -> new RuntimeException("Substitute teacher not found"));

        // Get all students in the department
        List<Student> departmentStudents = studentRepository.findByDepartmentId(timetable.getDepartmentId());
        List<String> studentIds = departmentStudents.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toList());

        // TEACHER NOTIFICATION
        Notification teacherNotification = new Notification();
        teacherNotification.setTitle("Teacher Substitution - " + department.getDepartmentName());
        teacherNotification.setMessage(String.format(
                "Substitute teacher assigned:\n" +
                        "Department: %s\n" +
                        "Original Teacher: %s\n" +
                        "Substitute: %s\n" +
                        "Date/Time: %s at %s\n" +
                        "Reason: %s",
                department.getDepartmentName(),
                originalTeacher.getFullName(),
                substituteTeacher.getFullName(),
                timetable.getDayOfWeek(),
                timetable.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                reason
        ));
        teacherNotification.setType("SUBSTITUTION");
        teacherNotification.setPriority("HIGH");
        teacherNotification.setRecipientIds(Arrays.asList(timetable.getTeacherId(), substituteTeacherId));
        teacherNotification.setRecipientType("TEACHER");
        teacherNotification.setDepartmentId(timetable.getDepartmentId());
        teacherNotification.setScheduledDate(LocalDateTime.now());

        // STUDENT NOTIFICATION
        Notification studentNotification = new Notification();
        studentNotification.setTitle("Substitute Teacher - " + department.getDepartmentName());
        studentNotification.setMessage(String.format(
                "Notice: Your %s department class will have a substitute teacher.\n\n" +
                        "Substitute Teacher: %s\n" +
                        "Date/Time: %s at %s\n" +
                        "Room: %s\n\n" +
                        "Please attend class as scheduled and show respect to the substitute teacher.",
                department.getDepartmentName(),
                substituteTeacher.getFullName(),
                timetable.getDayOfWeek(),
                timetable.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                timetable.getRoomNumber()
        ));
        studentNotification.setType("SUBSTITUTION");
        studentNotification.setPriority("MEDIUM");
        studentNotification.setRecipientIds(studentIds);
        studentNotification.setRecipientType("STUDENT");
        studentNotification.setDepartmentId(timetable.getDepartmentId());
        studentNotification.setScheduledDate(LocalDateTime.now());

        // Save and send both notifications
        notificationRepository.save(teacherNotification);
        notificationRepository.save(studentNotification);

        sendNotificationAsync(teacherNotification);
        sendNotificationAsync(studentNotification);
    }

    // Daily reminder scheduler - runs every day at 8:00 AM
    @Scheduled(cron = "0 0 8 * * ?") // 8:00 AM daily
    public void sendDailyReminders() {
        String today = LocalDateTime.now().getDayOfWeek().toString();
        List<Timetable> todaysTimetables = timetableRepository.findTodaysActiveTimetables(today);

        for (Timetable timetable : todaysTimetables) {
            Department department = departmentRepository.findById(timetable.getDepartmentId()).orElse(null);
            if (department == null) continue;

            // Get all students in the department
            List<Student> departmentStudents = studentRepository.findByDepartmentId(timetable.getDepartmentId());
            List<String> studentIds = departmentStudents.stream()
                    .map(Student::getStudentId)
                    .collect(Collectors.toList());

            // TEACHER REMINDER
            Notification teacherReminder = new Notification();
            teacherReminder.setTitle("Teaching Reminder - " + department.getDepartmentName());
            teacherReminder.setMessage(String.format(
                    "Reminder: You have %s department class today at %s in room %s.\n" +
                            "Department students: %d",
                    department.getDepartmentName(),
                    timetable.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    timetable.getRoomNumber(),
                    departmentStudents.size()
            ));
            teacherReminder.setType("DAILY_REMINDER");
            teacherReminder.setPriority("MEDIUM");
            teacherReminder.setRecipientIds(Arrays.asList(timetable.getTeacherId()));
            teacherReminder.setRecipientType("TEACHER");
            teacherReminder.setDepartmentId(timetable.getDepartmentId());
            teacherReminder.setScheduledDate(LocalDateTime.now());

            // STUDENT REMINDER
            if (!studentIds.isEmpty()) {
                Notification studentReminder = new Notification();
                studentReminder.setTitle("Class Reminder - " + department.getDepartmentName());
                studentReminder.setMessage(String.format(
                        "Reminder: You have %s department class today.\n\n" +
                                "Time: %s - %s\n" +
                                "Room: %s\n" +
                                "Teacher: %s\n\n" +
                                "Don't forget to bring your materials!",
                        department.getDepartmentName(),
                        timetable.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        timetable.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        timetable.getRoomNumber(),
                        getTeacherName(timetable.getTeacherId())
                ));
                studentReminder.setType("DAILY_REMINDER");
                studentReminder.setPriority("MEDIUM");
                studentReminder.setRecipientIds(studentIds);
                studentReminder.setRecipientType("STUDENT");
                studentReminder.setDepartmentId(timetable.getDepartmentId());
                studentReminder.setScheduledDate(LocalDateTime.now());

                notificationRepository.save(studentReminder);
                sendNotificationAsync(studentReminder);
            }

            notificationRepository.save(teacherReminder);
            sendNotificationAsync(teacherReminder);
        }
    }

    public List<Notification> getNotificationsForStudent(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get personal notifications
        List<Notification> personalNotifications = notificationRepository
                .findByRecipientIdsContaining(studentId);

        // Get department-wide student notifications
        Set<Notification> departmentNotifications = notificationRepository
                .findByDepartmentIdAndRecipientType(student.getDepartmentId(), "STUDENT");

        // Combine and remove duplicates
        Set<String> notificationIds = new HashSet<>();
        List<Notification> allNotifications = new ArrayList<>();

        Stream.of(personalNotifications, departmentNotifications)
                .flatMap(collection -> collection.stream())
                .forEach(n -> {
                    if (notificationIds.add(n.getNotificationId())) {
                        allNotifications.add(n);
                    }
                });

        return allNotifications.stream()
                .sorted((n1, n2) -> n2.getScheduledDate().compareTo(n1.getScheduledDate()))
                .collect(Collectors.toList());
    }

    public List<Notification> getNotificationsForTeacher(String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Get personal notifications
        List<Notification> personalNotifications = notificationRepository
                .findByRecipientIdsContaining(teacherId);

        // Get department-wide teacher notifications for ALL departments the teacher belongs to
        List<Notification> departmentNotifications = new ArrayList<>();

        if (teacher.getDepartmentId() != null && !teacher.getDepartmentId().isEmpty()) {
            for (String deptId : teacher.getDepartmentId()) {
                List<Notification> deptNotifs = (List<Notification>) notificationRepository
                        .findByDepartmentIdAndRecipientType(deptId, "TEACHER");
                departmentNotifications.addAll(deptNotifs);
            }
        }

        // Combine and remove duplicates
        Set<String> notificationIds = new HashSet<>();
        List<Notification> allNotifications = new ArrayList<>();

        Stream.of(personalNotifications, departmentNotifications)
                .flatMap(List::stream)
                .forEach(n -> {
                    if (notificationIds.add(n.getNotificationId())) {
                        allNotifications.add(n);
                    }
                });

        return allNotifications.stream()
                .sorted((n1, n2) -> n2.getScheduledDate().compareTo(n1.getScheduledDate()))
                .collect(Collectors.toList());
    }
    // Create exam/assignment notifications for department students
    public void createExamNotification(String departmentId, String examTitle, LocalDateTime examDate, String venue) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        List<Student> departmentStudents = studentRepository.findByDepartmentId(departmentId);
        List<String> studentIds = departmentStudents.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toList());

        Notification examNotification = new Notification();
        examNotification.setTitle("Exam Notification - " + department.getDepartmentName());
        examNotification.setMessage(String.format(
                "EXAM ALERT: %s\n\n" +
                        "Department: %s\n" +
                        "Date: %s\n" +
                        "Time: %s\n" +
                        "Venue: %s\n\n" +
                        "Please prepare accordingly and arrive 15 minutes early.",
                examTitle,
                department.getDepartmentName(),
                examDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")),
                examDate.format(DateTimeFormatter.ofPattern("HH:mm")),
                venue
        ));
        examNotification.setType("EXAM_NOTIFICATION");
        examNotification.setPriority("HIGH");
        examNotification.setRecipientIds(studentIds);
        examNotification.setRecipientType("STUDENT");
        examNotification.setDepartmentId(departmentId);
        examNotification.setScheduledDate(LocalDateTime.now());

        notificationRepository.save(examNotification);
        sendNotificationAsync(examNotification);
    }

    // Create general department announcement
    public void createDepartmentAnnouncement(String departmentId, String title, String message, String recipientType, String priority) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        List<String> recipientIds = new ArrayList<>();

        if ("STUDENT".equals(recipientType) || "ALL".equals(recipientType)) {
            List<Student> students = studentRepository.findByDepartmentId(departmentId);
            recipientIds.addAll(students.stream().map(Student::getStudentId).collect(Collectors.toList()));
        }

        if ("TEACHER".equals(recipientType) || "ALL".equals(recipientType)) {
            List<Teacher> teachers = teacherRepository.findByDepartmentId(departmentId);
            recipientIds.addAll(teachers.stream().map(Teacher::getTeacherId).collect(Collectors.toList()));
        }

        Notification announcement = new Notification();
        announcement.setTitle(title + " - " + department.getDepartmentName());
        announcement.setMessage(String.format(
                "Department: %s\n\n%s",
                department.getDepartmentName(),
                message
        ));
        announcement.setType("ANNOUNCEMENT");
        announcement.setPriority(priority != null ? priority : "MEDIUM");
        announcement.setRecipientIds(recipientIds);
        announcement.setRecipientType(recipientType);
        announcement.setDepartmentId(departmentId);
        announcement.setScheduledDate(LocalDateTime.now());

        notificationRepository.save(announcement);
        sendNotificationAsync(announcement);
    }

    private String getTeacherName(String teacherId) {
        return teacherRepository.findById(teacherId)
                .map(Teacher::getFullName)
                .orElse("Unknown Teacher");
    }

    // Process pending notifications - runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void processPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository
                .findPendingNotificationsToSend(LocalDateTime.now());

        for (Notification notification : pendingNotifications) {
            sendNotificationAsync(notification);
        }
    }

    @Async
    public void sendNotificationAsync(Notification notification) {
        try {
            // Send email notification
            emailService.sendNotificationEmail(notification);

            // Update notification status
            notification.setSent(true);
            notification.setStatus("SENT");
            notificationRepository.save(notification);

            log.info("Notification sent successfully: {}", notification.getNotificationId());
        } catch (Exception e) {
            notification.setStatus("FAILED");
            notificationRepository.save(notification);
            log.error("Failed to send notification: {}", notification.getNotificationId(), e);
        }
    }
}
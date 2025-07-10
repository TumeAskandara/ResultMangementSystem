package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.UserRepository;
import com.example.resultmanagementsystem.model.Complaint;
import com.example.resultmanagementsystem.model.Notification;
import com.example.resultmanagementsystem.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    public void sendNewComplaintNotification(Complaint complaint) {
        // Get teacher email based on course
        String teacherEmail = teacherService.getTeacherEmailByCourseId(complaint.getCourseId());
        String studentName = studentService.getStudentNameById(complaint.getStudentId());
        String courseName = teacherService.getCourseNameById(complaint.getCourseId());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(teacherEmail);
        message.setSubject("New Complaint Submitted: " + courseName);
        message.setText(
                "Dear Teacher,\n\n" +
                        "A new complaint has been submitted by " + studentName + " regarding " + courseName + ".\n\n" +
                        "Complaint Description: " + complaint.getDescription() + "\n\n" +
                        "You can respond to this complaint by visiting: " + baseUrl + "/complaints/" + complaint.getId() + "\n\n" +
                        "Please address this complaint at your earliest convenience.\n\n" +
                        "Regards,\nResult Management System"
        );

        mailSender.send(message);

        // Update notification status
        complaint.setTeacherNotified(true);
        complaint.setLastNotificationSent(LocalDateTime.now());
    }

    public void sendComplaintAssignedNotification(Complaint complaint) {
        String teacherEmail = teacherService.getTeacherEmailById(complaint.getAssignedToId());
        String teacherName = teacherService.getTeacherNameById(complaint.getAssignedToId());
        String courseName = teacherService.getCourseNameById(complaint.getCourseId());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(teacherEmail);
        message.setSubject("Complaint Assigned: " + courseName);
        message.setText(
                "Dear " + teacherName + ",\n\n" +
                        "A complaint has been assigned to you regarding " + courseName + ".\n\n" +
                        "Complaint Description: " + complaint.getDescription() + "\n\n" +
                        "You can respond to this complaint by visiting: " + baseUrl + "/complaints/" + complaint.getId() + "\n\n" +
                        "Please address this complaint at your earliest convenience.\n\n" +
                        "Regards,\nResult Management System"
        );

        mailSender.send(message);

        // Update notification status
        complaint.setTeacherNotified(true);
        complaint.setLastNotificationSent(LocalDateTime.now());
    }

    public void sendComplaintStatusUpdateNotification(Complaint complaint) {
        String studentEmail = studentService.getStudentEmailById(complaint.getStudentId());
        String studentName = studentService.getStudentNameById(complaint.getStudentId());
        String courseName = teacherService.getCourseNameById(complaint.getCourseId());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(studentEmail);
        message.setSubject("Update on Your Complaint: " + courseName);

        String statusText = "Your complaint has been " +
                (complaint.getStatus() == Complaint.ComplaintStatus.RESOLVED ? "resolved" :
                        complaint.getStatus() == Complaint.ComplaintStatus.REJECTED ? "rejected" :
                                "updated to " + complaint.getStatus().toString().toLowerCase().replace("_", " "));

        message.setText(
                "Dear " + studentName + ",\n\n" +
                        statusText + ".\n\n" +
                        "Complaint Description: " + complaint.getDescription() + "\n\n" +
                        (complaint.getResponse() != null && !complaint.getResponse().isEmpty() ?
                                "Response: " + complaint.getResponse() + "\n\n" : "") +
                        "You can view details by visiting: " + baseUrl + "/student/complaints/" + complaint.getId() + "\n\n" +
                        "If you have further questions, please submit a new complaint or contact the administration.\n\n" +
                        "Regards,\nResult Management System"
        );

        mailSender.send(message);

        // Update notification status
        complaint.setStudentNotified(true);
        complaint.setLastNotificationSent(LocalDateTime.now());
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String verificationLink = baseUrl + "/api/v1/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Verify Your Email");
        message.setText("Click the link to verify your email: " + verificationLink);

        mailSender.send(message);
    }

    public void sendNotificationEmail(Notification notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(getEmailAddresses(notification.getRecipientIds()));
            message.setSubject(notification.getTitle());
            message.setText(notification.getMessage());
            message.setFrom(fromEmail);

            mailSender.send(message);
            log.info("Email sent successfully for notification: {}", notification.getNotificationId());
        } catch (Exception e) {
            log.error("Failed to send email for notification: {}", notification.getNotificationId(), e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    public void sendBulkNotificationEmail(List<Notification> notifications) {
        for (Notification notification : notifications) {
            sendNotificationEmail(notification);
        }
    }

    private String[] getEmailAddresses(List<String> recipientIds) {
        if (recipientIds == null || recipientIds.isEmpty()) {
            return new String[0];
        }
        return recipientIds.stream()
                .map(id -> {
                    User user = userRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("User not found for ID: " + id));
                    return user.getEmail();
                })
                .toArray(String[]::new);
    }

    public void sendEmail(String toEmail, String toName, String subject, String messageBody) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(messageBody);

            mailSender.send(message);

            log.info("Email sent successfully to {} ({}): {}", toName, toEmail, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {} ({}): {}", toName, toEmail, subject, e);
            throw new RuntimeException("Failed to send email to " + toEmail, e);
        }

    }}
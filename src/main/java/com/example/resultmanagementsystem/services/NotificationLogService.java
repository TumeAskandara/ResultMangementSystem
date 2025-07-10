package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.NotificationLogRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.model.NotificationLog;
import com.example.resultmanagementsystem.model.Student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationLogService {

    private final JavaMailSender mailSender;
    private final StudentRepository studentRepository;
    private final NotificationLogRepository notificationLogRepository;

    public void sendNotification(List<String> studentIds, String message, String eventId) {
        try {
            List<Student> students = studentRepository.findAllById(studentIds);

            for (Student student : students) {
                sendEmailNotification(student.getEmail(), "School Calendar Update", message);
            }

            // Log successful notification
            NotificationLog log = new NotificationLog();
            log.setEventId(eventId);
            log.setRecipientStudentIds(studentIds);
            log.setMessage(message);
            log.setType(NotificationLog.NotificationType.EMAIL);
            log.setSuccessful(true);

            notificationLogRepository.save(log);

        } catch (Exception e) {
            log.error("Failed to send notifications for event: " + eventId, e);

            // Log failed notification
            NotificationLog errorLog = new NotificationLog();
            errorLog.setEventId(eventId);
            errorLog.setRecipientStudentIds(studentIds);
            errorLog.setMessage(message);
            errorLog.setType(NotificationLog.NotificationType.EMAIL);
            errorLog.setSuccessful(false);
            errorLog.setErrorMessage(e.getMessage());

            notificationLogRepository.save(errorLog);
        }
    }

    private void sendEmailNotification(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("noreply@school.com");

        mailSender.send(mailMessage);
    }
}


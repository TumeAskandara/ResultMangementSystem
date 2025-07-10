package com.example.resultmanagementsystem.services;


import com.example.resultmanagementsystem.Dto.Repository.ComplaintRepository;
import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.Dto.Repository.ResultRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.Dto.complainDTO.*;
import com.example.resultmanagementsystem.model.Complaint;
import com.example.resultmanagementsystem.model.Course;
import com.example.resultmanagementsystem.model.Result;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ResultRepository resultRepository;
    private final EmailService emailService;
    private final TeacherService teacherService;


    @Value("${app.admin.email:askanburi@gmail.com}")
    private String adminEmail;

    @Value("${app.admin.name:System Administrator}")
    private String adminName;

    @Value("${app.admin.notifications.enabled:true}")
    private boolean adminNotificationsEnabled;

    private static final String ADMIN_EMAIL = "askanburi@gmail.com";
    private static final String ADMIN_NAME = "System Administrator";


    public Complaint createComplaint(ComplaintDTO complaintDTO) {
        // Validate student exists
        if (!studentRepository.existsById(complaintDTO.getStudentId())) {
            throw new IllegalArgumentException("Student with ID " + complaintDTO.getStudentId() + " not found");
        }

        // Validate course exists
        if (!courseRepository.existsById(complaintDTO.getCourseId())) {
            throw new IllegalArgumentException("Course with ID " + complaintDTO.getCourseId() + " not found");
        }

        // Validate result exists if provided
        String resultId = complaintDTO.getResultId();
        if (resultId != null && !resultId.isEmpty()) {
            if (!resultRepository.existsById(resultId)) {
                throw new IllegalArgumentException("Result with ID " + resultId + " not found");
            }
        }

        // Validate description
        if (complaintDTO.getDescription() == null || complaintDTO.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Complaint description cannot be empty");
        }

        // Create new complaint
        Complaint complaint = new Complaint();
        complaint.setStudentId(complaintDTO.getStudentId());
        complaint.setCourseId(complaintDTO.getCourseId());
        complaint.setResultId(complaintDTO.getResultId());
        complaint.setDescription(complaintDTO.getDescription());
        complaint.setStatus(Complaint.ComplaintStatus.PENDING);

        // Get course's teacher ID and automatically assign
        Course course = courseRepository.findById(complaintDTO.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.getTeacherId() != null && !course.getTeacherId().isEmpty()) {
            complaint.setAssignedToId(course.getTeacherId());
            complaint.setAssignedAt(LocalDateTime.now());
        }

        // Save the complaint first
        Complaint savedComplaint = complaintRepository.save(complaint);

        // Send email notification
        try {
            emailService.sendNewComplaintNotification(savedComplaint);
        } catch (Exception e) {
            // Log error but don't fail the complaint creation
            System.err.println("Failed to send email notification: " + e.getMessage());
        }

        return savedComplaint;
    }

    public Complaint getComplaintById(String id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id: " + id));
    }

    public ComplaintDetailDTO getComplaintDetailsById(String id) {
        Complaint complaint = getComplaintById(id);

        // Get related data
        Student student = studentRepository.findById(complaint.getStudentId())
                .orElse(null);

        Course course = courseRepository.findById(complaint.getCourseId())
                .orElse(null);

        Result result = null;
        if (complaint.getResultId() != null && !complaint.getResultId().isEmpty()) {
            result = resultRepository.findById(complaint.getResultId())
                    .orElse(null);
        }

        // Create DTOs
        StudentSummaryDTO studentDTO = null;
        if (student != null) {
            studentDTO = new StudentSummaryDTO(
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    student.getRegistrationNumber()
            );
        }

        CourseSummaryDTO courseDTO = null;
        if (course != null) {
            courseDTO = new CourseSummaryDTO(
                    course.getId(),
                    course.getCode(),
                    course.getCourseTitle(),
                    course.getTeacherId()
            );
        }

        ResultSummaryDTO resultDTO = null;
        if (result != null) {
            resultDTO = new ResultSummaryDTO(
                    result.getId(),
                    result.getStudentId(),
                    result.getCourseId(),
                    result.getMarks(),
                    result.getGrade()
            );
        }

        TeacherSummaryDTO teacherDTO = null;
        if (complaint.getAssignedToId() != null) {
            teacherDTO = teacherService.getTeacherSummaryById(complaint.getAssignedToId());
        }

        return new ComplaintDetailDTO(complaint, studentDTO, courseDTO, resultDTO, teacherDTO);
    }

    public List<Complaint> getComplaintsByStudentId(String studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " not found");
        }
        return complaintRepository.findByStudentId(studentId);
    }

    public List<Complaint> getComplaintsByCourseId(String courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }
        return complaintRepository.findByCourseId(courseId);
    }

    public Page<Complaint> getComplaintsByStatus(Complaint.ComplaintStatus status, Pageable pageable) {
        return complaintRepository.findByStatus(status, pageable);
    }

    public List<Complaint> getComplaintsByTeacher(String teacherId) {
        if (!teacherService.teacherExists(teacherId)) {
            throw new IllegalArgumentException("Teacher with ID " + teacherId + " not found");
        }
        return complaintRepository.findByAssignedToId(teacherId);
    }

    public Page<Complaint> getComplaintsByTeacher(String teacherId, Pageable pageable) {
        if (!teacherService.teacherExists(teacherId)) {
            throw new IllegalArgumentException("Teacher with ID " + teacherId + " not found");
        }
        return complaintRepository.findByAssignedToId(teacherId, pageable);
    }

    public List<Complaint> getUnresolvedComplaintsByTeacher(String teacherId) {
        if (!teacherService.teacherExists(teacherId)) {
            throw new IllegalArgumentException("Teacher with ID " + teacherId + " not found");
        }
        return complaintRepository.findUnresolvedComplaintsByTeacher(teacherId);
    }

    public Complaint updateComplaintStatus(String id, String statusStr, String response) {
        Complaint.ComplaintStatus status;
        try {
            status = Complaint.ComplaintStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusStr +
                    ". Valid statuses are: " +
                    String.join(", ",
                            List.of(Complaint.ComplaintStatus.values())
                                    .stream()
                                    .map(Enum::name)
                                    .collect(Collectors.toList())
                    ));
        }

        Complaint complaint = getComplaintById(id);

        // Validate status transition
        if (!isValidStatusTransition(complaint.getStatus(), status)) {
            throw new IllegalArgumentException("Invalid status transition from " +
                    complaint.getStatus() + " to " + status);
        }

        complaint.setStatus(status);

        // Only update response if provided
        if (response != null && !response.trim().isEmpty()) {
            complaint.setResponse(response);
        }

        complaint.setUpdatedAt(LocalDateTime.now());

        if (status == Complaint.ComplaintStatus.RESOLVED || status == Complaint.ComplaintStatus.REJECTED) {
            complaint.setResolvedAt(LocalDateTime.now());
            // Reset student notification flag to send update
            complaint.setStudentNotified(false);
        }

        Complaint updatedComplaint = complaintRepository.save(complaint);

        // Send notification for status update
        if (status == Complaint.ComplaintStatus.RESOLVED || status == Complaint.ComplaintStatus.REJECTED) {
            try {
                emailService.sendComplaintStatusUpdateNotification(updatedComplaint);
            } catch (Exception e) {
                // Log error but don't fail the update
                System.err.println("Failed to send status update notification: " + e.getMessage());
            }
        }

        return updatedComplaint;
    }

    public Complaint assignComplaint(String id, String teacherId) {
        // Validate teacher exists
        if (!teacherService.teacherExists(teacherId)) {
            throw new IllegalArgumentException("Teacher with ID " + teacherId + " not found");
        }

        Complaint complaint = getComplaintById(id);

        // Only allow assignment for pending or under review complaints
        if (complaint.getStatus() == Complaint.ComplaintStatus.RESOLVED ||
                complaint.getStatus() == Complaint.ComplaintStatus.REJECTED) {
            throw new IllegalArgumentException("Cannot assign resolved or rejected complaints");
        }

        // Set assignment details
        complaint.setAssignedToId(teacherId);
        complaint.setAssignedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());

        // Update status if needed
        if (complaint.getStatus() == Complaint.ComplaintStatus.PENDING) {
            complaint.setStatus(Complaint.ComplaintStatus.UNDER_REVIEW);
        }

        // Reset teacher notification flag to send notification
        complaint.setTeacherNotified(false);

        Complaint updatedComplaint = complaintRepository.save(complaint);

        // Send assignment notification
        try {
            emailService.sendComplaintAssignedNotification(updatedComplaint);
        } catch (Exception e) {
            // Log error but don't fail the assignment
            System.err.println("Failed to send assignment notification: " + e.getMessage());
        }

        return updatedComplaint;
    }

    public void deleteComplaint(String id) {
        if (!complaintRepository.existsById(id)) {
            throw new RuntimeException("Complaint not found with id: " + id);
        }
        complaintRepository.deleteById(id);
    }

    // Fixed method to resolve variable name conflict
    public ComplaintDashboardDTO getComplaintsDashboard() {
        long totalComplaints = complaintRepository.count();
        long pendingCount = complaintRepository.findByStatus(Complaint.ComplaintStatus.PENDING).size();
        long underReviewCount = complaintRepository.findByStatus(Complaint.ComplaintStatus.UNDER_REVIEW).size();
        long resolvedCount = complaintRepository.findByStatus(Complaint.ComplaintStatus.RESOLVED).size();
        long rejectedCount = complaintRepository.findByStatus(Complaint.ComplaintStatus.REJECTED).size();

        // Calculate average resolution time
        List<Complaint> resolvedList = complaintRepository.findByStatus(Complaint.ComplaintStatus.RESOLVED);
        double averageResolutionTimeInHours = resolvedList.stream()
                .filter(c -> c.getCreatedAt() != null && c.getResolvedAt() != null)
                .mapToDouble(c -> Duration.between(c.getCreatedAt(), c.getResolvedAt()).toHours())
                .average()
                .orElse(0.0);

        // Get complaints from last week and month
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        long complaintsLastWeek = complaintRepository
                .findComplaintsByDateRange(oneWeekAgo, LocalDateTime.now()).size();

        long complaintsLastMonth = complaintRepository
                .findComplaintsByDateRange(oneMonthAgo, LocalDateTime.now()).size();

        return new ComplaintDashboardDTO(
                totalComplaints,
                pendingCount,
                underReviewCount,
                resolvedCount,
                rejectedCount,
                averageResolutionTimeInHours,
                complaintsLastWeek,
                complaintsLastMonth
        );
    }

    private boolean isValidStatusTransition(Complaint.ComplaintStatus currentStatus, Complaint.ComplaintStatus newStatus) {
        Map<Complaint.ComplaintStatus, List<Complaint.ComplaintStatus>> validTransitions = new HashMap<>();

        validTransitions.put(Complaint.ComplaintStatus.PENDING,
                List.of(Complaint.ComplaintStatus.UNDER_REVIEW, Complaint.ComplaintStatus.REJECTED));

        validTransitions.put(Complaint.ComplaintStatus.UNDER_REVIEW,
                List.of(Complaint.ComplaintStatus.RESOLVED, Complaint.ComplaintStatus.REJECTED));

        validTransitions.put(Complaint.ComplaintStatus.RESOLVED, List.of());
        validTransitions.put(Complaint.ComplaintStatus.REJECTED, List.of());

        return validTransitions.getOrDefault(currentStatus, List.of()).contains(newStatus);
    }

    // Fixed method in ComplaintService.java
    @Scheduled(fixedDelay = 3600000) // Run every hour
    public void sendPendingNotifications() {
        // Find complaints that need teacher notifications
        List<Complaint> teacherComplaints = complaintRepository.findUnnotifiedComplaintsForTeachers();
        for (Complaint complaint : teacherComplaints) {
            try {
                // Check if complaint is assigned to a teacher before sending notification
                if (complaint.getAssignedToId() != null && !complaint.getAssignedToId().trim().isEmpty()) {
                    // Verify the teacher exists before sending notification
                    if (teacherService.teacherExists(complaint.getAssignedToId())) {
                        emailService.sendNewComplaintNotification(complaint);
                        complaint.setTeacherNotified(true);
                        complaint.setLastNotificationSent(LocalDateTime.now());
                        complaintRepository.save(complaint);
                    } else {
                        System.err.println("Teacher with ID " + complaint.getAssignedToId() +
                                " not found for complaint " + complaint.getId());
                    }
                } else {
                    // Log that complaint is not assigned to any teacher
                    System.out.println("Complaint " + complaint.getId() +
                            " is not assigned to any teacher yet - skipping notification");
                }
            } catch (Exception e) {
                // Log error but continue with other notifications
                System.err.println("Failed to send teacher notification for complaint " +
                        complaint.getId() + ": " + e.getMessage());
            }
        }

        // Find complaints that need student notifications
        List<Complaint> studentComplaints = complaintRepository.findUnnotifiedComplaintsForStudents();
        for (Complaint complaint : studentComplaints) {
            try {
                // Check if student ID exists before sending notification
                if (complaint.getStudentId() != null && !complaint.getStudentId().trim().isEmpty()) {
                    // Verify the student exists before sending notification
                    if (studentRepository.existsById(complaint.getStudentId())) {
                        emailService.sendComplaintStatusUpdateNotification(complaint);
                        complaint.setStudentNotified(true);
                        complaint.setLastNotificationSent(LocalDateTime.now());
                        complaintRepository.save(complaint);
                    } else {
                        System.err.println("Student with ID " + complaint.getStudentId() +
                                " not found for complaint " + complaint.getId());
                    }
                } else {
                    System.err.println("Student ID is null for complaint " + complaint.getId());
                }
            } catch (Exception e) {
                // Log error but continue with other notifications
                System.err.println("Failed to send student notification for complaint " +
                        complaint.getId() + ": " + e.getMessage());
            }
        }
    }

    @Scheduled(fixedDelay = 7200000) // Run every 2 hours
//    public void handleUnassignedComplaints() {
//        List<Complaint> unassignedComplaints = complaintRepository.findUnassignedComplaints();
//
//        for (Complaint complaint : unassignedComplaints) {
//            try {
//                // Auto-assign based on course if possible
//                Course course = courseRepository.findById(complaint.getCourseId()).orElse(null);
//
//                if (course != null && course.getTeacherId() != null && !course.getTeacherId().isEmpty()) {
//                    // Verify teacher exists before auto-assigning
//                    if (teacherService.teacherExists(course.getTeacherId())) {
//                        // Auto-assign to course teacher
//                        complaint.setAssignedToId(course.getTeacherId());
//                        complaint.setAssignedAt(LocalDateTime.now());
//                        complaint.setStatus(Complaint.ComplaintStatus.UNDER_REVIEW);
//                        complaint.setTeacherNotified(false); // Mark for teacher notification
//
//                        complaintRepository.save(complaint);
//
//                        System.out.println("Auto-assigned complaint " + complaint.getId() +
//                                " to teacher " + course.getTeacherId());
//                    } else {
//                        // Teacher doesn't exist, notify admin
//                        sendAdminNotificationForUnassignedComplaint(complaint,
//                                "Course teacher ID " + course.getTeacherId() + " not found");
//                    }
//                } else {
//                    // No teacher found for course, notify admin
//                    String reason = (course == null) ? "Course not found" : "No teacher assigned to course";
//                    sendAdminNotificationForUnassignedComplaint(complaint, reason);
//                }
//            } catch (Exception e) {
//                System.err.println("Failed to handle unassigned complaint " +
//                        complaint.getId() + ": " + e.getMessage());
//
//                // Send admin notification about the error
//                try {
//                    sendAdminNotificationForUnassignedComplaint(complaint,
//                            "Error processing complaint: " + e.getMessage());
//                } catch (Exception emailError) {
//                    System.err.println("Failed to send admin notification: " + emailError.getMessage());
//                }
//            }
//        }
//    }

    /**
     * Send email notification to admin for unassigned complaints
     */
//    private void sendAdminNotificationForUnassignedComplaint(Complaint complaint, String reason) {
//        if (!adminNotificationsEnabled) {
//            System.out.println("Admin notifications disabled - skipping notification for complaint " + complaint.getId());
//            return;
//        }
//
//        try {
//            // Get student and course details for context
//            Student student = studentRepository.findById(complaint.getStudentId()).orElse(null);
//            Course course = courseRepository.findById(complaint.getCourseId()).orElse(null);
//
//            String subject = "URGENT: Unassigned Complaint Requires Manual Assignment - ID: " + complaint.getId();
//
//            StringBuilder emailBody = new StringBuilder();
//            emailBody.append("Dear ").append(adminName).append(",\n\n");
//            emailBody.append("A complaint requires immediate attention and manual assignment.\n\n");
//
//            emailBody.append("=== COMPLAINT DETAILS ===\n");
//            emailBody.append("Complaint ID: ").append(complaint.getId()).append("\n");
//            emailBody.append("Status: ").append(complaint.getStatus()).append("\n");
//            emailBody.append("Created: ").append(complaint.getCreatedAt()).append("\n");
//            emailBody.append("Last Updated: ").append(complaint.getUpdatedAt()).append("\n");
//            emailBody.append("Reason for Admin Notification: ").append(reason).append("\n\n");
//
//            if (student != null) {
//                emailBody.append("=== STUDENT DETAILS ===\n");
//                emailBody.append("Student ID: ").append(student.getStudentId()).append("\n");
//                emailBody.append("Name: ").append(student.getName()).append("\n");
//                emailBody.append("Email: ").append(student.getEmail()).append("\n");
//                emailBody.append("Registration Number: ").append(student.getRegistrationNumber()).append("\n\n");
//            } else {
//                emailBody.append("=== STUDENT DETAILS ===\n");
//                emailBody.append("Student ID: ").append(complaint.getStudentId()).append("\n");
//                emailBody.append("WARNING: Student details not found in database!\n\n");
//            }
//
//            if (course != null) {
//                emailBody.append("=== COURSE DETAILS ===\n");
//                emailBody.append("Course ID: ").append(course.getId()).append("\n");
//                emailBody.append("Course Code: ").append(course.getCode()).append("\n");
//                emailBody.append("Course Title: ").append(course.getCourseTitle()).append("\n");
//                emailBody.append("Assigned Teacher ID: ").append(course.getTeacherId() != null ? course.getTeacherId() : "None").append("\n");
//                emailBody.append("Credits: ").append(course.getCredits()).append("\n\n");
//            } else {
//                emailBody.append("=== COURSE DETAILS ===\n");
//                emailBody.append("Course ID: ").append(complaint.getCourseId()).append("\n");
//                emailBody.append("WARNING: Course details not found in database!\n\n");
//            }
//
//            // Add result details if complaint is about a specific result
//            if (complaint.getResultId() != null && !complaint.getResultId().isEmpty()) {
//                Result result = resultRepository.findById(complaint.getResultId()).orElse(null);
//                emailBody.append("=== RESULT DETAILS ===\n");
//                if (result != null) {
//                    emailBody.append("Result ID: ").append(result.getId()).append("\n");
//                    emailBody.append("Marks: ").append(result.getMarks()).append("\n");
//                    emailBody.append("Grade: ").append(result.getGrade()).append("\n");
//                    emailBody.append("Semester: ").append(result.getSemester()).append("\n\n");
//                } else {
//                    emailBody.append("Result ID: ").append(complaint.getResultId()).append("\n");
//                    emailBody.append("WARNING: Result details not found in database!\n\n");
//                }
//            }
//
//            emailBody.append("=== COMPLAINT DESCRIPTION ===\n");
//            emailBody.append(complaint.getDescription()).append("\n\n");
//
//            if (complaint.getResponse() != null && !complaint.getResponse().isEmpty()) {
//                emailBody.append("=== CURRENT RESPONSE ===\n");
//                emailBody.append(complaint.getResponse()).append("\n\n");
//            }
//
//            emailBody.append("=== ACTION REQUIRED ===\n");
//            emailBody.append("1. Log into the Result Management System\n");
//            emailBody.append("2. Navigate to the Complaints Management section\n");
//            emailBody.append("3. Search for complaint ID: ").append(complaint.getId()).append("\n");
//            emailBody.append("4. Manually assign this complaint to an appropriate teacher\n");
//            emailBody.append("5. Update the complaint status if necessary\n\n");
//
//            emailBody.append("=== SYSTEM INFORMATION ===\n");
//            emailBody.append("Notification sent: ").append(LocalDateTime.now()).append("\n");
//            emailBody.append("System: Result Management System\n");
//            emailBody.append("Environment: ").append(System.getProperty("spring.profiles.active", "default")).append("\n\n");
//
//            emailBody.append("This is an automated notification. Please do not reply to this email.\n\n");
//            emailBody.append("Best regards,\n");
//            emailBody.append("Result Management System");
//
//            // Send email to admin
//            emailService.sendEmail(adminEmail, adminName, subject, emailBody.toString());
//
//            System.out.println("Admin notification sent successfully for complaint " + complaint.getId() +
//                    " - Reason: " + reason + " - Sent to: " + adminEmail);
//
//        } catch (Exception e) {
//            System.err.println("Failed to send admin notification for complaint " +
//                    complaint.getId() + ": " + e.getMessage());
//            e.printStackTrace(); // Add stack trace for debugging
//        }
//    }

    @Scheduled(cron = "0 0 9 * * ?") // Daily at 9 AM
    public void sendDailyUnassignedComplaintsSummary() {
        if (!adminNotificationsEnabled) {
            return;
        }

        try {
            List<Complaint> unassignedComplaints = complaintRepository.findUnassignedComplaints();

            if (unassignedComplaints.isEmpty()) {
                return; // No unassigned complaints, no need to send summary
            }

            String subject = "Daily Summary: " + unassignedComplaints.size() + " Unassigned Complaints Require Attention";

            StringBuilder emailBody = new StringBuilder();
            emailBody.append("Dear ").append(adminName).append(",\n\n");
            emailBody.append("Here's your daily summary of unassigned complaints that require manual assignment.\n\n");
            emailBody.append("=== SUMMARY ===\n");
            emailBody.append("Total Unassigned Complaints: ").append(unassignedComplaints.size()).append("\n");
            emailBody.append("Report Date: ").append(LocalDateTime.now().toLocalDate()).append("\n\n");

            emailBody.append("=== COMPLAINT LIST ===\n");
            for (int i = 0; i < unassignedComplaints.size(); i++) {
                Complaint complaint = unassignedComplaints.get(i);
                Student student = studentRepository.findById(complaint.getStudentId()).orElse(null);
                Course course = courseRepository.findById(complaint.getCourseId()).orElse(null);

                emailBody.append((i + 1)).append(". Complaint ID: ").append(complaint.getId()).append("\n");
                emailBody.append("   Status: ").append(complaint.getStatus()).append("\n");
                emailBody.append("   Created: ").append(complaint.getCreatedAt().toLocalDate()).append("\n");
                emailBody.append("   Student: ").append(student != null ? student.getName() : "Unknown").append("\n");
                emailBody.append("   Course: ").append(course != null ? course.getCode() + " - " + course.getCourseTitle() : "Unknown").append("\n");
                emailBody.append("   Description: ").append(complaint.getDescription().length() > 100 ?
                        complaint.getDescription().substring(0, 100) + "..." : complaint.getDescription()).append("\n\n");
            }

            emailBody.append("=== ACTION REQUIRED ===\n");
            emailBody.append("Please review and assign these complaints as soon as possible.\n");
            emailBody.append("Log into the system to manage these complaints.\n\n");

            emailBody.append("Best regards,\n");
            emailBody.append("Result Management System");

            emailService.sendEmail(adminEmail, adminName, subject, emailBody.toString());

            System.out.println("Daily summary sent to admin: " + unassignedComplaints.size() + " unassigned complaints");

        } catch (Exception e) {
            System.err.println("Failed to send daily summary to admin: " + e.getMessage());
        }




}

    // Add this method to handle daily summary with duplicate prevention
//    private void sendDailySummaryIfNeeded(String triggerReason) {
//        LocalDate today = LocalDate.now();
//        if (lastDailySummarySent == null || !lastDailySummarySent.equals(today)) {
//            try {
//                sendDailyUnassignedComplaintsSummary();
//                lastDailySummarySent = today;
//                System.out.println("Daily summary sent - Trigger: " + triggerReason);
//            } catch (Exception e) {
//                System.err.println("Failed to send daily summary: " + e.getMessage());
//            }
//        } else {
//            System.out.println("Daily summary already sent today, skipping - Trigger: " + triggerReason);
//        }
//    }

}
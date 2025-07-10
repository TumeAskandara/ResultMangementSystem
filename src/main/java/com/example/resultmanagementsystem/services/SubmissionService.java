//package com.example.resultmanagementsystem.services;
//
//import com.example.resultmanagementsystem.Dto.Repository.AssignmentRepository;
//import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
//import com.example.resultmanagementsystem.Dto.Repository.SubmissionRepository;
//import com.example.resultmanagementsystem.Dto.SubmissionCreateDTO;
//import com.example.resultmanagementsystem.model.Assignment;
//import com.example.resultmanagementsystem.model.Student;
//import com.example.resultmanagementsystem.model.Submission;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
////public class SubmissionService {
////
////    private final SubmissionRepository submissionRepository;
////    private final AssignmentRepository assignmentRepository;
////    private final StudentRepository studentRepository;
////
////    public Submission submitAssignment(String studentId, SubmissionCreateDTO dto) {
////        Student student = studentRepository.findById(studentId)
////                .orElseThrow(() -> new RuntimeException("Student not found"));
////
////        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
////                .orElseThrow(() -> new RuntimeException("Assignment not found"));
////
////        // If student.getDepartmentId() returns a String and assignment has Set<String>
////        if (!assignment.getDepartmentId().contains(student.getDepartmentId().toString())) {
////            throw new RuntimeException("Student not authorized to submit this assignment");
////        }
////
////        // Check if assignment is still active and not overdue
////        if (!assignment.isActive()) {
////            throw new RuntimeException("Assignment is no longer active");
////        }
////
////        boolean isLate = assignment.getDueDate().isBefore(LocalDateTime.now());
////
////        // Check if submission already exists (for resubmission)
////        Optional<Submission> existingSubmission = submissionRepository
////                .findByAssignmentIdAndStudentId(dto.getAssignmentId(), studentId);
////
////        Submission submission;
////        if (existingSubmission.isPresent()) {
////            // Update existing submission
////            submission = existingSubmission.get();
////            submission.setFileUrls(dto.getFileUrls());
////            submission.setComments(dto.getComments());
////            submission.setUpdatedAt(LocalDateTime.now());
////            submission.setLate(isLate);
////            // Reset grade and feedback for resubmission
////            submission.setGrade(null);
////            submission.setFeedback(null);
////        } else {
////            // Create new submission
////            submission = new Submission();
////            submission.setAssignmentId(dto.getAssignmentId());
////            submission.setStudentId(studentId);
////            submission.setStudentName(student.getName());
////            submission.setFileUrls(dto.getFileUrls());
////            submission.setComments(dto.getComments());
////            submission.setLate(isLate);
////        }
////
////        return submissionRepository.save(submission);
////    }
////
////    public List<Submission> getSubmissionsForAssignment(String assignmentId, String teacherId) {
////        Assignment assignment = assignmentRepository.findById(assignmentId)
////                .orElseThrow(() -> new RuntimeException("Assignment not found"));
////
////        if (!assignment.getTeacherId().equals(teacherId)) {
////            throw new RuntimeException("Unauthorized to view submissions for this assignment");
////        }
////
////        return submissionRepository.findByAssignmentId(assignmentId);
////    }
////
////    public List<Submission> getSubmissionsForStudent(String studentId) {
////        return submissionRepository.findByStudentId(studentId);
////    }
////
////    public Submission gradeSubmission(String submissionId, String teacherId, Integer grade, String feedback) {
////        Submission submission = submissionRepository.findById(submissionId)
////                .orElseThrow(() -> new RuntimeException("Submission not found"));
////
////        Assignment assignment = assignmentRepository.findById(submission.getAssignmentId())
////                .orElseThrow(() -> new RuntimeException("Assignment not found"));
////
////        if (!assignment.getTeacherId().equals(teacherId)) {
////            throw new RuntimeException("Unauthorized to grade this submission");
////        }
////
////        if (grade < 0 || grade > assignment.getMaxPoints()) {
////            throw new RuntimeException("Grade must be between 0 and " + assignment.getMaxPoints());
////        }
////
////        submission.setGrade(grade);
////        submission.setFeedback(feedback);
////        submission.setUpdatedAt(LocalDateTime.now());
////
////        return submissionRepository.save(submission);
////    }
////
////    public Submission getSubmissionByAssignmentAndStudent(String assignmentId, String studentId) {
////        return submissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
////                .orElse(null);
////    }
////}


package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.AssignmentRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.Dto.Repository.SubmissionRepository;
import com.example.resultmanagementsystem.Dto.Repository.TeacherRepository;
import com.example.resultmanagementsystem.Dto.SubmissionCreateDTO;
import com.example.resultmanagementsystem.model.Assignment;
import com.example.resultmanagementsystem.model.Student;
import com.example.resultmanagementsystem.model.Submission;
import com.example.resultmanagementsystem.model.Teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public Submission submitAssignment(String studentId, SubmissionCreateDTO dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Check if student belongs to the same department as assignment
        if (!assignment.getDepartmentId().contains(student.getDepartmentId().toString())) {
            throw new RuntimeException("Student not authorized to submit this assignment");
        }

        // Check if assignment is still active and not overdue
        if (!assignment.isActive()) {
            throw new RuntimeException("Assignment is no longer active");
        }

        boolean isLate = assignment.getDueDate().isBefore(LocalDateTime.now());

        // Check if submission already exists (for resubmission)
        Optional<Submission> existingSubmission = submissionRepository
                .findByAssignmentIdAndStudentId(dto.getAssignmentId(), studentId);

        Submission submission;
        if (existingSubmission.isPresent()) {
            // Update existing submission
            submission = existingSubmission.get();
            submission.setFileUrls(dto.getFileUrls());
            submission.setComments(dto.getComments());
            submission.setUpdatedAt(LocalDateTime.now());
            submission.setLate(isLate);
            // Reset grade and feedback for resubmission
            submission.setGrade(null);
            submission.setFeedback(null);
        } else {
            // Create new submission
            submission = new Submission();
            submission.setAssignmentId(dto.getAssignmentId());
            submission.setStudentId(studentId);
            submission.setStudentName(student.getName());
            submission.setFileUrls(dto.getFileUrls());
            submission.setComments(dto.getComments());
            submission.setLate(isLate);
        }

        return submissionRepository.save(submission);
    }

    // Get all submissions for assignments in teacher's department
    public List<Submission> getSubmissionsForDepartment(String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Get all assignments in teacher's department
        List<Assignment> departmentAssignments = assignmentRepository
                .findByDepartmentIdAndIsActiveTrue(teacher.getDepartmentId());

        // Get all submissions for these assignments
        List<String> assignmentIds = departmentAssignments.stream()
                .map(Assignment::getAssignmentId)
                .toList();

        return submissionRepository.findByAssignmentIdIn(assignmentIds);
    }

    // Get submissions for a specific assignment (department-based access)
    public List<Submission> getSubmissionsForAssignment(String assignmentId, String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Check if teacher belongs to same department as assignment
        if (!assignment.getDepartmentId().contains(teacher.getDepartmentId().toString())) {
            throw new RuntimeException("Unauthorized to view submissions for this assignment");
        }

        return submissionRepository.findByAssignmentId(assignmentId);
    }

    public List<Submission> getSubmissionsForStudent(String studentId) {
        return submissionRepository.findByStudentId(studentId);
    }

    // Updated grading method - based on department access
    public Submission gradeSubmission(String submissionId, String teacherId, Integer grade, String feedback) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        Assignment assignment = assignmentRepository.findById(submission.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Check if teacher belongs to same department as assignment (not just assignment owner)
        if (!assignment.getDepartmentId().contains(teacher.getDepartmentId().toString())) {
            throw new RuntimeException("Unauthorized to grade this submission - not in same department");
        }

        if (grade < 0 || grade > assignment.getMaxPoints()) {
            throw new RuntimeException("Grade must be between 0 and " + assignment.getMaxPoints());
        }

        submission.setGrade(grade);
        submission.setFeedback(feedback);
        submission.setGradedBy(teacherId);
        submission.setGradedByName(teacher.getFirstname() + " " + teacher.getLastname());
        submission.setUpdatedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }

    public Submission getSubmissionByAssignmentAndStudent(String assignmentId, String studentId) {
        return submissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
                .orElse(null);
    }

    // New method: Get all ungraded submissions in department
    public List<Submission> getUngradedSubmissionsForDepartment(String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Get all assignments in teacher's department
        List<Assignment> departmentAssignments = assignmentRepository
                .findByDepartmentIdAndIsActiveTrue(teacher.getDepartmentId());

        List<String> assignmentIds = departmentAssignments.stream()
                .map(Assignment::getAssignmentId)
                .toList();

        // Get ungraded submissions for these assignments
        return submissionRepository.findByAssignmentIdInAndGradeIsNull(assignmentIds);
    }

    // New method: Get graded submissions in department
    public List<Submission> getGradedSubmissionsForDepartment(String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<Assignment> departmentAssignments = assignmentRepository
                .findByDepartmentIdAndIsActiveTrue(teacher.getDepartmentId());

        List<String> assignmentIds = departmentAssignments.stream()
                .map(Assignment::getAssignmentId)
                .toList();

        return submissionRepository.findByAssignmentIdInAndGradeIsNotNull(assignmentIds);
    }
}
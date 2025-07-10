package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.AssignmentCreateDTO;
import com.example.resultmanagementsystem.Dto.AssignmentResponseDTO;
import com.example.resultmanagementsystem.Dto.Repository.*;
import com.example.resultmanagementsystem.model.Assignment;

import com.example.resultmanagementsystem.model.Student;
import com.example.resultmanagementsystem.model.Submission;
import com.example.resultmanagementsystem.model.Teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final SubmissionRepository submissionRepository;

    public Assignment createAssignment(String teacherId, AssignmentCreateDTO dto) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Assignment assignment = new Assignment();
        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        assignment.setDepartmentId(teacher.getDepartmentId());
        assignment.setTeacherId(teacherId);
        assignment.setTeacherName(teacher.getFirstname());
        assignment.setDueDate(dto.getDueDate());
        assignment.setMaxPoints(dto.getMaxPoints());
        assignment.setAttachmentUrls(dto.getAttachmentUrls());

        return assignmentRepository.save(assignment);
    }

    public List<AssignmentResponseDTO> getAssignmentsForStudent(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Assignment> assignments = assignmentRepository
                .findByDepartmentIdContainingAndIsActiveTrue(student.getDepartmentId());

        return assignments.stream()
                .map(assignment -> mapToResponseDTO(assignment, studentId))
                .collect(Collectors.toList());
    }

    public List<Assignment> getAssignmentsForTeacher(String teacherId) {
        return assignmentRepository.findByTeacherIdAndIsActiveTrue(teacherId);
    }

    public Assignment getAssignmentById(String assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public Assignment updateAssignment(String assignmentId, String teacherId, AssignmentCreateDTO dto) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Unauthorized to update this assignment");
        }

        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        assignment.setDueDate(dto.getDueDate());
        assignment.setMaxPoints(dto.getMaxPoints());
        assignment.setAttachmentUrls(dto.getAttachmentUrls());
        assignment.setUpdatedAt(LocalDateTime.now());

        return assignmentRepository.save(assignment);
    }

    public void deleteAssignment(String assignmentId, String teacherId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Unauthorized to delete this assignment");
        }

        assignment.setActive(false);
        assignment.setUpdatedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);
    }

    private AssignmentResponseDTO mapToResponseDTO(Assignment assignment, String studentId) {
        AssignmentResponseDTO dto = new AssignmentResponseDTO();
        dto.setAssignmentId(assignment.getAssignmentId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setTeacherName(assignment.getTeacherName());
        dto.setDueDate(assignment.getDueDate());
        dto.setMaxPoints(assignment.getMaxPoints());
        dto.setAttachmentUrls(assignment.getAttachmentUrls());
        dto.setCreatedAt(assignment.getCreatedAt());
        dto.setOverdue(assignment.getDueDate().isBefore(LocalDateTime.now()));

        // Get department name
        departmentRepository.findById(String.valueOf(assignment.getDepartmentId()))
                .ifPresent(dept -> dto.setDepartmentName(dept.getDepartmentName()));

        // Check submission status
        Optional<Submission> submission = submissionRepository
                .findByAssignmentIdAndStudentId(assignment.getAssignmentId(), studentId);

        if (submission.isPresent()) {
            dto.setSubmitted(true);
            if (submission.get().getGrade() != null) {
                dto.setSubmissionStatus("GRADED");
            } else {
                dto.setSubmissionStatus("SUBMITTED");
            }
        } else {
            dto.setSubmitted(false);
            dto.setSubmissionStatus("NOT_SUBMITTED");
        }

        return dto;
    }
}

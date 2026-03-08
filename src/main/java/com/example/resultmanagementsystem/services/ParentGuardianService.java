package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.ChildSummaryDTO;
import com.example.resultmanagementsystem.Dto.ParentDashboardDTO;
import com.example.resultmanagementsystem.Dto.ParentGuardianDTO;
import com.example.resultmanagementsystem.Dto.Repository.AttendanceSummaryRepository;
import com.example.resultmanagementsystem.Dto.Repository.DepartmentRepository;
import com.example.resultmanagementsystem.Dto.Repository.ParentGuardianRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.model.AttendanceSummary;
import com.example.resultmanagementsystem.model.Department;
import com.example.resultmanagementsystem.model.ParentGuardian;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParentGuardianService {

    private final ParentGuardianRepository parentGuardianRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final AttendanceSummaryRepository attendanceSummaryRepository;

    public ParentGuardian createParentGuardian(ParentGuardianDTO dto) {
        // Check if email already exists
        Optional<ParentGuardian> existing = parentGuardianRepository.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Parent with this email already exists.");
        }

        ParentGuardian parent = new ParentGuardian();
        parent.setFirstName(dto.getFirstName());
        parent.setLastName(dto.getLastName());
        parent.setEmail(dto.getEmail());
        parent.setPhone(dto.getPhone());
        parent.setAddress(dto.getAddress());
        parent.setRelationship(dto.getRelationship());
        parent.setOccupation(dto.getOccupation());
        if (dto.getStudentIds() != null) {
            parent.setStudentIds(dto.getStudentIds());
        }
        parent.setCreatedAt(LocalDateTime.now());
        parent.setUpdatedAt(LocalDateTime.now());

        return parentGuardianRepository.save(parent);
    }

    public ParentGuardian getParentById(String id) {
        return parentGuardianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + id));
    }

    public ParentGuardian getParentByEmail(String email) {
        return parentGuardianRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Parent not found with email: " + email));
    }

    public ParentGuardian getParentByUserId(String userId) {
        return parentGuardianRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Parent not found with user ID: " + userId));
    }

    public List<ParentGuardian> getParentsByStudentId(String studentId) {
        return parentGuardianRepository.findByStudentIdsContaining(studentId);
    }

    public ParentGuardian updateParent(String id, ParentGuardianDTO dto) {
        ParentGuardian parent = getParentById(id);

        parent.setFirstName(dto.getFirstName());
        parent.setLastName(dto.getLastName());
        parent.setEmail(dto.getEmail());
        parent.setPhone(dto.getPhone());
        parent.setAddress(dto.getAddress());
        parent.setRelationship(dto.getRelationship());
        parent.setOccupation(dto.getOccupation());
        if (dto.getStudentIds() != null) {
            parent.setStudentIds(dto.getStudentIds());
        }
        parent.setUpdatedAt(LocalDateTime.now());

        return parentGuardianRepository.save(parent);
    }

    public ParentGuardian linkStudentToParent(String parentId, String studentId) {
        ParentGuardian parent = getParentById(parentId);

        // Verify student exists
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        parent.getStudentIds().add(studentId);
        parent.setUpdatedAt(LocalDateTime.now());

        return parentGuardianRepository.save(parent);
    }

    public ParentGuardian unlinkStudentFromParent(String parentId, String studentId) {
        ParentGuardian parent = getParentById(parentId);

        if (!parent.getStudentIds().contains(studentId)) {
            throw new RuntimeException("Student " + studentId + " is not linked to this parent.");
        }

        parent.getStudentIds().remove(studentId);
        parent.setUpdatedAt(LocalDateTime.now());

        return parentGuardianRepository.save(parent);
    }

    public ParentDashboardDTO getParentDashboard(String parentId) {
        ParentGuardian parent = getParentById(parentId);
        ParentDashboardDTO dashboard = new ParentDashboardDTO();
        dashboard.setParentId(parentId);

        List<ChildSummaryDTO> children = new ArrayList<>();

        for (String studentId : parent.getStudentIds()) {
            ChildSummaryDTO childSummary = new ChildSummaryDTO();
            childSummary.setStudentId(studentId);

            // Get student info
            Student student = studentRepository.findByStudentId(studentId);
            if (student != null) {
                childSummary.setStudentName(student.getName());

                // Get department name
                if (student.getDepartmentId() != null) {
                    Department dept = departmentRepository.findByDepartmentId(student.getDepartmentId());
                    if (dept != null) {
                        childSummary.setDepartmentName(dept.getDepartmentName());
                    }
                }

                // Get attendance percentage (average across all courses)
                List<AttendanceSummary> summaries = attendanceSummaryRepository
                        .findByStudentIdAndAcademicYearAndSemester(studentId, String.valueOf(LocalDateTime.now().getYear()), null);
                if (summaries != null && !summaries.isEmpty()) {
                    double avgPercentage = summaries.stream()
                            .mapToDouble(AttendanceSummary::getAttendancePercentage)
                            .average()
                            .orElse(0.0);
                    childSummary.setAttendancePercentage(avgPercentage);
                }
            }

            // Initialize empty lists for data that may come from other services
            childSummary.setRecentResults(new ArrayList<>());
            childSummary.setUpcomingEvents(new ArrayList<>());

            children.add(childSummary);
        }

        dashboard.setChildren(children);
        return dashboard;
    }

    public Page<ParentGuardian> getAllParents(Pageable pageable) {
        return parentGuardianRepository.findAll(pageable);
    }
}

package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.LearningPathDTO;
import com.example.resultmanagementsystem.Dto.LearningPathProgressDTO;
import com.example.resultmanagementsystem.Dto.Repository.LearningPathProgressRepository;
import com.example.resultmanagementsystem.Dto.Repository.LearningPathRepository;
import com.example.resultmanagementsystem.model.LearningPath;
import com.example.resultmanagementsystem.model.LearningPathProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LearningPathService {

    private final LearningPathRepository learningPathRepository;
    private final LearningPathProgressRepository learningPathProgressRepository;

    public LearningPath createLearningPath(LearningPathDTO dto) {
        LearningPath learningPath = LearningPath.builder()
                .id(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .departmentId(dto.getDepartmentId())
                .courseIds(dto.getCourseIds())
                .prerequisites(dto.getPrerequisites())
                .difficulty(dto.getDifficulty())
                .estimatedDuration(dto.getEstimatedDuration())
                .enrolledStudents(new HashSet<>())
                .completedStudents(new HashSet<>())
                .createdBy(dto.getCreatedBy())
                .isPublished(false)
                .tags(dto.getTags())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return learningPathRepository.save(learningPath);
    }

    public LearningPath getLearningPathById(String id) {
        return learningPathRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Learning path not found with ID: " + id));
    }

    public Page<LearningPath> getPublishedLearningPaths(Pageable pageable) {
        return learningPathRepository.findByIsPublished(true, pageable);
    }

    public List<LearningPath> getLearningPathsByDepartment(String departmentId) {
        return learningPathRepository.findByDepartmentId(departmentId);
    }

    public LearningPathProgress enrollStudent(String pathId, String studentId) {
        LearningPath learningPath = getLearningPathById(pathId);

        // Check if already enrolled
        Optional<LearningPathProgress> existing = learningPathProgressRepository
                .findByStudentIdAndLearningPathId(studentId, pathId);
        if (existing.isPresent()) {
            throw new RuntimeException("Student is already enrolled in this learning path");
        }

        // Add student to enrolled list
        if (learningPath.getEnrolledStudents() == null) {
            learningPath.setEnrolledStudents(new HashSet<>());
        }
        learningPath.getEnrolledStudents().add(studentId);
        learningPath.setUpdatedAt(LocalDateTime.now());
        learningPathRepository.save(learningPath);

        // Create progress record
        LearningPathProgress progress = LearningPathProgress.builder()
                .id(UUID.randomUUID().toString())
                .learningPathId(pathId)
                .studentId(studentId)
                .currentCourseIndex(0)
                .completedCourseIds(new HashSet<>())
                .progressPercentage(0.0)
                .startedAt(LocalDateTime.now())
                .lastAccessedAt(LocalDateTime.now())
                .status(LearningPathProgress.ProgressStatus.IN_PROGRESS)
                .build();

        return learningPathProgressRepository.save(progress);
    }

    public LearningPathProgress updateProgress(String pathId, String studentId, String completedCourseId) {
        LearningPath learningPath = getLearningPathById(pathId);
        LearningPathProgress progress = learningPathProgressRepository
                .findByStudentIdAndLearningPathId(studentId, pathId)
                .orElseThrow(() -> new RuntimeException("Progress not found for student: " + studentId
                        + " and learning path: " + pathId));

        if (progress.getCompletedCourseIds() == null) {
            progress.setCompletedCourseIds(new HashSet<>());
        }
        progress.getCompletedCourseIds().add(completedCourseId);

        // Update current course index
        List<String> courseIds = learningPath.getCourseIds();
        if (courseIds != null) {
            int completedIndex = courseIds.indexOf(completedCourseId);
            if (completedIndex >= 0 && completedIndex + 1 > progress.getCurrentCourseIndex()) {
                progress.setCurrentCourseIndex(Math.min(completedIndex + 1, courseIds.size()));
            }

            // Calculate progress percentage
            double percentage = (progress.getCompletedCourseIds().size() * 100.0) / courseIds.size();
            progress.setProgressPercentage(Math.round(percentage * 100.0) / 100.0);

            // Check if completed
            if (progress.getCompletedCourseIds().containsAll(courseIds)) {
                progress.setStatus(LearningPathProgress.ProgressStatus.COMPLETED);
                progress.setCompletedAt(LocalDateTime.now());

                // Add to completed students
                if (learningPath.getCompletedStudents() == null) {
                    learningPath.setCompletedStudents(new HashSet<>());
                }
                learningPath.getCompletedStudents().add(studentId);
                learningPathRepository.save(learningPath);
            }
        }

        progress.setLastAccessedAt(LocalDateTime.now());
        return learningPathProgressRepository.save(progress);
    }

    public LearningPathProgressDTO getStudentProgress(String studentId, String pathId) {
        LearningPathProgress progress = learningPathProgressRepository
                .findByStudentIdAndLearningPathId(studentId, pathId)
                .orElseThrow(() -> new RuntimeException("Progress not found for student: " + studentId
                        + " and learning path: " + pathId));
        return toProgressDTO(progress);
    }

    public List<LearningPathProgressDTO> getStudentAllPaths(String studentId) {
        return learningPathProgressRepository.findByStudentId(studentId).stream()
                .map(this::toProgressDTO)
                .collect(Collectors.toList());
    }

    public LearningPath publishLearningPath(String id) {
        LearningPath learningPath = getLearningPathById(id);
        learningPath.setPublished(true);
        learningPath.setUpdatedAt(LocalDateTime.now());
        return learningPathRepository.save(learningPath);
    }

    public LearningPath updateLearningPath(String id, LearningPathDTO dto) {
        LearningPath existing = getLearningPathById(id);

        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getDepartmentId() != null) existing.setDepartmentId(dto.getDepartmentId());
        if (dto.getCourseIds() != null) existing.setCourseIds(dto.getCourseIds());
        if (dto.getPrerequisites() != null) existing.setPrerequisites(dto.getPrerequisites());
        if (dto.getDifficulty() != null) existing.setDifficulty(dto.getDifficulty());
        if (dto.getEstimatedDuration() > 0) existing.setEstimatedDuration(dto.getEstimatedDuration());
        if (dto.getTags() != null) existing.setTags(dto.getTags());
        existing.setUpdatedAt(LocalDateTime.now());

        return learningPathRepository.save(existing);
    }

    private LearningPathProgressDTO toProgressDTO(LearningPathProgress progress) {
        return LearningPathProgressDTO.builder()
                .id(progress.getId())
                .learningPathId(progress.getLearningPathId())
                .studentId(progress.getStudentId())
                .currentCourseIndex(progress.getCurrentCourseIndex())
                .completedCourseIds(progress.getCompletedCourseIds())
                .progressPercentage(progress.getProgressPercentage())
                .startedAt(progress.getStartedAt())
                .lastAccessedAt(progress.getLastAccessedAt())
                .completedAt(progress.getCompletedAt())
                .status(progress.getStatus())
                .build();
    }
}

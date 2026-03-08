package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.CourseModuleDTO;
import com.example.resultmanagementsystem.Dto.LearningProgressDTO;
import com.example.resultmanagementsystem.Dto.LessonDTO;
import com.example.resultmanagementsystem.Dto.Repository.CourseModuleRepository;
import com.example.resultmanagementsystem.Dto.Repository.LearningProgressRepository;
import com.example.resultmanagementsystem.Dto.Repository.LessonRepository;
import com.example.resultmanagementsystem.model.CourseModule;
import com.example.resultmanagementsystem.model.LearningProgress;
import com.example.resultmanagementsystem.model.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final CourseModuleRepository courseModuleRepository;
    private final LessonRepository lessonRepository;
    private final LearningProgressRepository learningProgressRepository;

    // ========== Module Operations ==========

    public CourseModule createModule(CourseModuleDTO dto) {
        CourseModule module = new CourseModule();
        module.setId(UUID.randomUUID().toString());
        module.setCourseId(dto.getCourseId());
        module.setTitle(dto.getTitle());
        module.setDescription(dto.getDescription());
        module.setOrderIndex(dto.getOrderIndex());
        module.setPublished(dto.isPublished());
        module.setPrerequisiteModuleId(dto.getPrerequisiteModuleId());
        module.setCreatedBy(dto.getCreatedBy());
        module.setCreatedAt(LocalDateTime.now());
        module.setUpdatedAt(LocalDateTime.now());
        return courseModuleRepository.save(module);
    }

    public List<CourseModule> getModulesByCourse(String courseId) {
        return courseModuleRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
    }

    public CourseModule updateModule(String id, CourseModuleDTO dto) {
        CourseModule module = courseModuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course module not found with id: " + id));
        module.setCourseId(dto.getCourseId());
        module.setTitle(dto.getTitle());
        module.setDescription(dto.getDescription());
        module.setOrderIndex(dto.getOrderIndex());
        module.setPublished(dto.isPublished());
        module.setPrerequisiteModuleId(dto.getPrerequisiteModuleId());
        module.setUpdatedAt(LocalDateTime.now());
        return courseModuleRepository.save(module);
    }

    public void deleteModule(String id) {
        if (!courseModuleRepository.existsById(id)) {
            throw new RuntimeException("Course module not found with id: " + id);
        }
        courseModuleRepository.deleteById(id);
    }

    public void reorderModules(String courseId, List<String> moduleIds) {
        for (int i = 0; i < moduleIds.size(); i++) {
            CourseModule module = courseModuleRepository.findById(moduleIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Module not found"));
            module.setOrderIndex(i);
            module.setUpdatedAt(LocalDateTime.now());
            courseModuleRepository.save(module);
        }
    }

    // ========== Lesson Operations ==========

    public Lesson createLesson(LessonDTO dto) {
        Lesson lesson = new Lesson();
        lesson.setId(UUID.randomUUID().toString());
        lesson.setModuleId(dto.getModuleId());
        lesson.setCourseId(dto.getCourseId());
        lesson.setTitle(dto.getTitle());
        lesson.setContentType(dto.getContentType());
        lesson.setContent(dto.getContent());
        lesson.setVideoUrl(dto.getVideoUrl());
        lesson.setDuration(dto.getDuration());
        lesson.setOrderIndex(dto.getOrderIndex());
        lesson.setPublished(dto.isPublished());
        lesson.setAttachments(dto.getAttachments());
        lesson.setCreatedBy(dto.getCreatedBy());
        lesson.setCreatedAt(LocalDateTime.now());
        lesson.setUpdatedAt(LocalDateTime.now());
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsByModule(String moduleId) {
        return lessonRepository.findByModuleIdOrderByOrderIndexAsc(moduleId);
    }

    public List<Lesson> getLessonsByCourse(String courseId) {
        return lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
    }

    public Lesson updateLesson(String id, LessonDTO dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + id));
        lesson.setModuleId(dto.getModuleId());
        lesson.setCourseId(dto.getCourseId());
        lesson.setTitle(dto.getTitle());
        lesson.setContentType(dto.getContentType());
        lesson.setContent(dto.getContent());
        lesson.setVideoUrl(dto.getVideoUrl());
        lesson.setDuration(dto.getDuration());
        lesson.setOrderIndex(dto.getOrderIndex());
        lesson.setPublished(dto.isPublished());
        lesson.setAttachments(dto.getAttachments());
        lesson.setUpdatedAt(LocalDateTime.now());
        return lessonRepository.save(lesson);
    }

    public void deleteLesson(String id) {
        if (!lessonRepository.existsById(id)) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
    }

    // ========== Progress Tracking ==========

    public LearningProgress trackProgress(String studentId, String lessonId, LearningProgressDTO dto) {
        LearningProgress progress = learningProgressRepository
                .findByStudentIdAndLessonId(studentId, lessonId)
                .orElseGet(() -> {
                    LearningProgress newProgress = new LearningProgress();
                    newProgress.setId(UUID.randomUUID().toString());
                    newProgress.setStudentId(studentId);
                    newProgress.setLessonId(lessonId);
                    newProgress.setStartedAt(LocalDateTime.now());
                    return newProgress;
                });

        progress.setCourseId(dto.getCourseId());
        progress.setModuleId(dto.getModuleId());
        progress.setStatus(dto.getStatus());
        progress.setProgressPercentage(dto.getProgressPercentage());
        progress.setTimeSpentMinutes(progress.getTimeSpentMinutes() + dto.getTimeSpentMinutes());
        progress.setLastAccessedAt(LocalDateTime.now());

        if (dto.getStatus() == LearningProgress.ProgressStatus.COMPLETED) {
            progress.setCompletedAt(LocalDateTime.now());
            progress.setProgressPercentage(100.0);
        }

        return learningProgressRepository.save(progress);
    }

    public List<LearningProgress> getStudentProgress(String studentId, String courseId) {
        return learningProgressRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    public Map<String, Object> getCourseCompletionPercentage(String studentId, String courseId) {
        List<Lesson> allLessons = lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        List<LearningProgress> progressList = learningProgressRepository.findByStudentIdAndCourseId(studentId, courseId);

        long completedCount = progressList.stream()
                .filter(p -> p.getStatus() == LearningProgress.ProgressStatus.COMPLETED)
                .count();

        double completionPercentage = allLessons.isEmpty() ? 0.0 :
                (double) completedCount / allLessons.size() * 100.0;

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", studentId);
        response.put("courseId", courseId);
        response.put("totalLessons", allLessons.size());
        response.put("completedLessons", completedCount);
        response.put("completionPercentage", completionPercentage);
        return response;
    }
}

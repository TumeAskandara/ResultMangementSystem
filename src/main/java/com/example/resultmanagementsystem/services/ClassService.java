package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.ClassPromotionDTO;
import com.example.resultmanagementsystem.Dto.ClassSectionDTO;
import com.example.resultmanagementsystem.Dto.ClassStatsDTO;
import com.example.resultmanagementsystem.Dto.Repository.ClassPromotionRepository;
import com.example.resultmanagementsystem.Dto.Repository.ClassSectionRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.model.ClassPromotion;
import com.example.resultmanagementsystem.model.ClassSection;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClassService {

    private final ClassSectionRepository classSectionRepository;
    private final ClassPromotionRepository classPromotionRepository;
    private final StudentRepository studentRepository;

    public ClassSection createClass(ClassSectionDTO dto) {
        ClassSection classSection = ClassSection.builder()
                .id(UUID.randomUUID().toString())
                .className(dto.getClassName())
                .section(dto.getSection())
                .departmentId(dto.getDepartmentId())
                .academicYear(dto.getAcademicYear())
                .classTeacherId(dto.getClassTeacherId())
                .classTeacherName(dto.getClassTeacherName())
                .capacity(dto.getCapacity())
                .currentStrength(0)
                .studentIds(new HashSet<>())
                .courseIds(new HashSet<>())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return classSectionRepository.save(classSection);
    }

    public ClassSection getClassById(String id) {
        return classSectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + id));
    }

    public List<ClassSection> getClassesByDepartment(String departmentId, String academicYear) {
        return classSectionRepository.findByDepartmentIdAndAcademicYear(departmentId, academicYear);
    }

    public List<ClassSection> getClassesByTeacher(String teacherId) {
        return classSectionRepository.findByClassTeacherId(teacherId);
    }

    public ClassSection updateClass(String id, ClassSectionDTO dto) {
        ClassSection existing = getClassById(id);

        if (dto.getClassName() != null) existing.setClassName(dto.getClassName());
        if (dto.getSection() != null) existing.setSection(dto.getSection());
        if (dto.getDepartmentId() != null) existing.setDepartmentId(dto.getDepartmentId());
        if (dto.getAcademicYear() != null) existing.setAcademicYear(dto.getAcademicYear());
        if (dto.getClassTeacherId() != null) existing.setClassTeacherId(dto.getClassTeacherId());
        if (dto.getClassTeacherName() != null) existing.setClassTeacherName(dto.getClassTeacherName());
        if (dto.getCapacity() > 0) existing.setCapacity(dto.getCapacity());
        existing.setUpdatedAt(LocalDateTime.now());

        return classSectionRepository.save(existing);
    }

    public ClassSection addStudentToClass(String classId, String studentId) {
        ClassSection classSection = getClassById(classId);

        if (classSection.getCurrentStrength() >= classSection.getCapacity()) {
            throw new RuntimeException("Class has reached its maximum capacity");
        }

        if (classSection.getStudentIds() == null) {
            classSection.setStudentIds(new HashSet<>());
        }

        if (classSection.getStudentIds().contains(studentId)) {
            throw new RuntimeException("Student is already in this class");
        }

        classSection.getStudentIds().add(studentId);
        classSection.setCurrentStrength(classSection.getStudentIds().size());
        classSection.setUpdatedAt(LocalDateTime.now());

        return classSectionRepository.save(classSection);
    }

    public ClassSection removeStudentFromClass(String classId, String studentId) {
        ClassSection classSection = getClassById(classId);

        if (classSection.getStudentIds() == null || !classSection.getStudentIds().contains(studentId)) {
            throw new RuntimeException("Student is not in this class");
        }

        classSection.getStudentIds().remove(studentId);
        classSection.setCurrentStrength(classSection.getStudentIds().size());
        classSection.setUpdatedAt(LocalDateTime.now());

        return classSectionRepository.save(classSection);
    }

    public ClassSection addCourseToClass(String classId, String courseId) {
        ClassSection classSection = getClassById(classId);

        if (classSection.getCourseIds() == null) {
            classSection.setCourseIds(new HashSet<>());
        }

        classSection.getCourseIds().add(courseId);
        classSection.setUpdatedAt(LocalDateTime.now());

        return classSectionRepository.save(classSection);
    }

    public ClassSection removeCourseFromClass(String classId, String courseId) {
        ClassSection classSection = getClassById(classId);

        if (classSection.getCourseIds() == null || !classSection.getCourseIds().contains(courseId)) {
            throw new RuntimeException("Course is not assigned to this class");
        }

        classSection.getCourseIds().remove(courseId);
        classSection.setUpdatedAt(LocalDateTime.now());

        return classSectionRepository.save(classSection);
    }

    public List<Student> getClassStudents(String classId) {
        ClassSection classSection = getClassById(classId);
        if (classSection.getStudentIds() == null || classSection.getStudentIds().isEmpty()) {
            return new ArrayList<>();
        }

        return classSection.getStudentIds().stream()
                .map(studentRepository::findByStudentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ClassPromotion promoteStudent(ClassPromotionDTO dto) {
        ClassPromotion promotion = ClassPromotion.builder()
                .id(UUID.randomUUID().toString())
                .studentId(dto.getStudentId())
                .studentName(dto.getStudentName())
                .fromClassId(dto.getFromClassId())
                .fromClassName(dto.getFromClassName())
                .toClassId(dto.getToClassId())
                .toClassName(dto.getToClassName())
                .fromAcademicYear(dto.getFromAcademicYear())
                .toAcademicYear(dto.getToAcademicYear())
                .promotionType(dto.getPromotionType())
                .promotedBy(dto.getPromotedBy())
                .remarks(dto.getRemarks())
                .promotionDate(LocalDateTime.now())
                .build();

        // Remove student from old class
        if (dto.getFromClassId() != null) {
            try {
                removeStudentFromClass(dto.getFromClassId(), dto.getStudentId());
            } catch (RuntimeException ignored) {
                // Student may not be in the from class
            }
        }

        // Add student to new class if promoted
        if (dto.getToClassId() != null && dto.getPromotionType() == ClassPromotion.PromotionType.PROMOTED) {
            addStudentToClass(dto.getToClassId(), dto.getStudentId());
        }

        return classPromotionRepository.save(promotion);
    }

    public List<ClassPromotion> bulkPromote(String fromClassId, String toClassId,
                                            String toAcademicYear, List<String> studentIds) {
        ClassSection fromClass = getClassById(fromClassId);
        ClassSection toClass = getClassById(toClassId);

        List<ClassPromotion> promotions = new ArrayList<>();
        for (String studentId : studentIds) {
            Student student = studentRepository.findByStudentId(studentId);
            String studentName = student != null ? student.getName() : "Unknown";

            ClassPromotionDTO dto = ClassPromotionDTO.builder()
                    .studentId(studentId)
                    .studentName(studentName)
                    .fromClassId(fromClassId)
                    .fromClassName(fromClass.getClassName() + " " + fromClass.getSection())
                    .toClassId(toClassId)
                    .toClassName(toClass.getClassName() + " " + toClass.getSection())
                    .fromAcademicYear(fromClass.getAcademicYear())
                    .toAcademicYear(toAcademicYear)
                    .promotionType(ClassPromotion.PromotionType.PROMOTED)
                    .promotedBy("system")
                    .remarks("Bulk promotion")
                    .build();

            promotions.add(promoteStudent(dto));
        }

        return promotions;
    }

    public List<ClassPromotion> getPromotionHistory(String studentId) {
        return classPromotionRepository.findByStudentId(studentId);
    }

    public ClassStatsDTO getClassStatistics(String classId) {
        ClassSection classSection = getClassById(classId);

        return ClassStatsDTO.builder()
                .classId(classId)
                .className(classSection.getClassName())
                .section(classSection.getSection())
                .capacity(classSection.getCapacity())
                .currentStrength(classSection.getCurrentStrength())
                .availableSlots(classSection.getCapacity() - classSection.getCurrentStrength())
                .totalCourses(classSection.getCourseIds() != null ? classSection.getCourseIds().size() : 0)
                .averageAttendance(0.0)
                .averageGPA(0.0)
                .classTeacherName(classSection.getClassTeacherName())
                .build();
    }
}

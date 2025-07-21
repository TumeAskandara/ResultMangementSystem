package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.Dto.Repository.TeacherRepository;
import com.example.resultmanagementsystem.Dto.TeacherDTO;
import com.example.resultmanagementsystem.Dto.complainDTO.TeacherSummaryDTO;
import com.example.resultmanagementsystem.model.Course;
import com.example.resultmanagementsystem.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    public Teacher createTeacher(Teacher teacher) {
        if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        return teacherRepository.save(teacher);
    }

    public Teacher getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found with email: " + email));
    }

    public Teacher getTeacherById(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher updateTeacher(String id, TeacherDTO teacherDTO) {
        Teacher teacher = getTeacherById(id);
        teacher.setFirstname(teacherDTO.getFirstname());
        teacher.setLastname(teacherDTO.getLastname());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setDepartmentId(teacherDTO.getDepartmentId());
        teacher.setRole(teacherDTO.getRole());

        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(String id) {
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Teacher not found");
        }
        teacherRepository.deleteById(id);
    }

    // Added methods to support EmailService
    public String getTeacherEmailById(String id) {
        Teacher teacher = getTeacherById(id);
        return teacher.getEmail();
    }

    public String getTeacherNameById(String id) {
        Teacher teacher = getTeacherById(id);
        return teacher.getFirstname() + " " + teacher.getLastname();
    }

    public String getTeacherEmailByCourseId(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return getTeacherEmailById(course.getTeacherId());
    }

    public String getCourseNameById(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getCourseTitle();
    }

    // Added method to support ComplaintService
    public boolean teacherExists(String teacherId) {
        return teacherRepository.existsById(teacherId);
    }

    // Added method to support ComplaintService
    public TeacherSummaryDTO getTeacherSummaryById(String id) {
        Teacher teacher = getTeacherById(id);
        return new TeacherSummaryDTO(
                teacher.getId(),
                teacher.getFirstname() + " " + teacher.getLastname(),
                teacher.getEmail(),
                teacher.getRole()
        );
    }
}
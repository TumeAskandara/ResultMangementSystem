package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.Dto.Repository.ResultRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ResultRepository resultRepository;
    private final CourseRepository courseRepository;

    public Student createStudent(Student student) {
        Optional<Student> existingStudentByEmail = studentRepository.findByEmail(student.getEmail());

        if (existingStudentByEmail.isPresent()) {
            throw new RuntimeException("Student with email already exists.");
        }

        // Check if name exists
        Student existingStudentByName = studentRepository.findByName(student.getName());
        if (existingStudentByName != null) {
            throw new RuntimeException("Student with name already exists.");
        }

        // Ensure student has departmentId set
        if (student.getDepartmentId() == null || student.getDepartmentId().isEmpty()) {
            throw new RuntimeException("Department ID is required.");
        }

        return studentRepository.save(student);
    }


    public Student getStudentById(String studentId) {
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        // Call getStudentByEmail using the email from the found student
        try {
            // This will validate if the email exists in the database
            getStudentIdByEmail(student.getEmail());
        } catch (RuntimeException e) {
            // If there's an inconsistency, you might want to log it
            System.out.println("Warning: Student found by ID but not by email: " + student.getEmail());
        }

        return student;
    }
    public List<Student> getStudentByDepartmentId(String departmentId){
        return studentRepository.findByDepartmentId(departmentId);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void deleteStudent(String studentId) {
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
        studentRepository.delete(student);
    }

    // Added methods to support EmailService
    public String getStudentNameById(String studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
        return student.getName();
    }

    public String getStudentEmailById(String studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
        return student.getEmail();
    }
    public Student getStudentIdByEmail(String email){
        Optional<Student> student = studentRepository.findByEmail(email);
        if(student.isEmpty()){
            throw new RuntimeException("Student not found with email: "+ email);
        }

        return student.get();
    }

    public Student getStudentDepartmentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
        return student;
    }
}
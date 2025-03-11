package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.Dto.Repository.ResultRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final ResultRepository resultRepository;

    private final CourseRepository courseRepository;


    public Student createStudent(Student student, String email, String name, String departmentId) {
        Student existingStudentByEmail = studentRepository.findByEmail(email);
        Student existingStudentByName = studentRepository.findByName(name);

        if (existingStudentByEmail != null || existingStudentByName != null) {
            throw new RuntimeException("Student with email or name already exists.");
        }

        return studentRepository.save(student);
    }



    public Student getStudentById(String studentId) {
        return studentRepository.findByStudentId(studentId);
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

}
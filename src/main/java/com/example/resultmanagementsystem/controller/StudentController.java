package com.example.resultmanagementsystem.controller;

import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.Dto.Repository.ResultRepository;
import com.example.resultmanagementsystem.Dto.Repository.StudentRepository;
import com.example.resultmanagementsystem.model.Result;
import com.example.resultmanagementsystem.model.Student;
import com.example.resultmanagementsystem.services.StudentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*", maxAge = 3600)
@Component
public class StudentController {

    private final StudentService studentService;

    private final StudentRepository studentRepository;


    @PostMapping("/createStudent")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student,
                    student.getEmail(),
                    student.getName(),
                    student.getDepartmentId());
            return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("getStudentById")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("deleteStudent")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        try {
            studentService.deleteStudent(studentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }
}
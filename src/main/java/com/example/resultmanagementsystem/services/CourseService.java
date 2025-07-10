package com.example.resultmanagementsystem.services;

import com.example.resultmanagementsystem.Dto.Repository.CourseRepository;
import com.example.resultmanagementsystem.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get a course by ID
    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    public List<Course> getCoursesByDepartmentId(String departmentId){
        return courseRepository.findCourseByDepartmentId(departmentId);
    }


    // Create a new course
    public Course createCourse(Course course) {
        String courseTitle = course.getCourseTitle();
        Course existingCourse = courseRepository.findByCourseTitle(courseTitle);
        if (existingCourse !=null){
            throw new RuntimeException("Course "+ courseTitle + "already exists");
        }
        return courseRepository.save(course);
    }

    // Update a course
    public Course updateCourse(String id, Course updatedCourse) {
        if (courseRepository.existsById(id)) {
            return courseRepository.save(updatedCourse);
        }
        return null;
    }

    // Delete a course
    public boolean deleteCourse(String id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}



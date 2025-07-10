package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Result;
import com.example.resultmanagementsystem.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResultRepository extends MongoRepository<Result, String> {
    List<Result> findByCourseId(String courseId);
    List<Result> findByStudentId(String studentId);

    List<Result> findByStudentIdAndSemesterAndYear(String studentId, String semester, String year);


}

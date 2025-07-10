package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {

    List<Complaint> findByStudentId(String studentId);

    List<Complaint> findByCourseId(String courseId);

    List<Complaint> findByStatus(Complaint.ComplaintStatus status);

    Page<Complaint> findByStatus(Complaint.ComplaintStatus status, Pageable pageable);

    List<Complaint> findByAssignedToId(String teacherId);

    Page<Complaint> findByAssignedToId(String teacherId, Pageable pageable);

    @Query("{'status': { $in: ['PENDING', 'UNDER_REVIEW'] }}")
    List<Complaint> findAllUnresolvedComplaints();

    @Query("{'status': { $in: ['PENDING', 'UNDER_REVIEW'] }, 'assignedToId': ?0}")
    List<Complaint> findUnresolvedComplaintsByTeacher(String teacherId);

    @Query("{'createdAt': { $gte: ?0, $lte: ?1 }}")
    List<Complaint> findComplaintsByDateRange(LocalDateTime start, LocalDateTime end);

    @Query("{'teacherNotified': false, 'status': 'PENDING'}")
    List<Complaint> findUnnotifiedComplaintsForTeachers();

    @Query("{'studentNotified': false, 'status': { $in: ['RESOLVED', 'REJECTED'] }}")
    List<Complaint> findUnnotifiedComplaintsForStudents();

    // Add this missing method for finding unassigned complaints
    @Query("{'assignedToId': { $exists: false }}")
    List<Complaint> findUnassignedComplaints();

    // Alternative query if assignedToId can be null or empty string
    // @Query("{ $or: [{'assignedToId': { $exists: false }}, {'assignedToId': null}, {'assignedToId': ''}]}")
    // List<Complaint> findUnassignedComplaints();
}
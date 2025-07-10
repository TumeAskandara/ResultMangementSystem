package com.example.resultmanagementsystem.Dto.Repository;


import com.example.resultmanagementsystem.model.Timetable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends MongoRepository<Timetable, String> {

    List<Timetable> findByDepartmentId(String departmentId);
    List<Timetable> findByTeacherId(String teacherId);
    List<Timetable> findBySemester(String semester);
    List<Timetable> findByDepartmentIdAndSemester(String departmentId, String semester);
    List<Timetable> findByDayOfWeek(String dayOfWeek);
    List<Timetable> findByDepartmentIdAndDayOfWeek(String departmentId, String dayOfWeek);
    List<Timetable> findByTeacherIdAndDayOfWeek(String teacherId, String dayOfWeek);
    List<Timetable> findByAcademicYear(String academicYear);
    List<Timetable> findByStatus(String status);
    List<Timetable> findByDepartmentIdAndSemesterAndSection(String departmentId, String semester, String section);

    // NEW: Substitute-related queries
    List<Timetable> findBySubstituteTeacherId(String substituteTeacherId);
    List<Timetable> findByIsSubstituted(Boolean isSubstituted);
    List<Timetable> findBySubstitutionDate(LocalDate substitutionDate);

    // NEW: Notification-related queries
    List<Timetable> findByNotificationSent(Boolean notificationSent);

    @Query("{'teacherId': ?0, 'dayOfWeek': ?1, 'startTime': {$lte: ?3}, 'endTime': {$gte: ?2}}")
    List<Timetable> findConflictingSchedules(String teacherId, String dayOfWeek, LocalTime startTime, LocalTime endTime);

    @Query("{'roomNumber': ?0, 'dayOfWeek': ?1, 'startTime': {$lte: ?3}, 'endTime': {$gte: ?2}}")
    List<Timetable> findRoomConflicts(String roomNumber, String dayOfWeek, LocalTime startTime, LocalTime endTime);

    // NEW: Find timetables for today's reminders
    @Query("{'dayOfWeek': ?0, 'status': 'ACTIVE'}")
    List<Timetable> findTodaysActiveTimetables(String dayOfWeek);
}
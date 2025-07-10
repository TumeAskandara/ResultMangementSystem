package com.example.resultmanagementsystem.services;


import com.example.resultmanagementsystem.Dto.Repository.TimetableRepository;
import com.example.resultmanagementsystem.model.Timetable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final NotificationService notificationService;

    public Timetable createTimetable(Timetable timetable) {
        if (hasTeacherConflict(timetable)) {
            throw new RuntimeException("Teacher has a conflicting schedule");
        }

        if (hasRoomConflict(timetable)) {
            throw new RuntimeException("Room is already booked for this time slot");
        }

        timetable.setCreatedDate(LocalDate.now());
        timetable.setUpdatedDate(LocalDate.now());
        Timetable savedTimetable = timetableRepository.save(timetable);

        notificationService.createScheduleChangeNotification(savedTimetable, "CREATED");
        return savedTimetable;
    }

    public Timetable updateTimetable(String timetableId, Timetable updatedTimetable) {
        return timetableRepository.findById(timetableId)
                .map(timetable -> {
                    boolean hasSignificantChange =
                            !timetable.getTeacherId().equals(updatedTimetable.getTeacherId()) ||
                                    !timetable.getStartTime().equals(updatedTimetable.getStartTime()) ||
                                    !timetable.getEndTime().equals(updatedTimetable.getEndTime()) ||
                                    !timetable.getDayOfWeek().equals(updatedTimetable.getDayOfWeek()) ||
                                    !timetable.getRoomNumber().equals(updatedTimetable.getRoomNumber());

                    if (hasSignificantChange) {
                        if (hasTeacherConflict(updatedTimetable, timetableId)) {
                            throw new RuntimeException("Teacher has a conflicting schedule");
                        }

                        if (hasRoomConflict(updatedTimetable, timetableId)) {
                            throw new RuntimeException("Room is already booked for this time slot");
                        }
                    }

                    // Update all fields
                    timetable.setDepartmentId(updatedTimetable.getDepartmentId());
                    timetable.setTeacherId(updatedTimetable.getTeacherId());
                    timetable.setSemester(updatedTimetable.getSemester());
                    timetable.setSubject(updatedTimetable.getSubject());
                    timetable.setDayOfWeek(updatedTimetable.getDayOfWeek());
                    timetable.setStartTime(updatedTimetable.getStartTime());
                    timetable.setEndTime(updatedTimetable.getEndTime());
                    timetable.setRoomNumber(updatedTimetable.getRoomNumber());
                    timetable.setCourseCode(updatedTimetable.getCourseCode());
                    timetable.setCredits(updatedTimetable.getCredits());
                    timetable.setAcademicYear(updatedTimetable.getAcademicYear());
                    timetable.setSection(updatedTimetable.getSection());
                    timetable.setSessionType(updatedTimetable.getSessionType());
                    timetable.setStatus(updatedTimetable.getStatus());
                    timetable.setUpdatedDate(LocalDate.now());

                    Timetable savedTimetable = timetableRepository.save(timetable);

                    if (hasSignificantChange) {
                        notificationService.createScheduleChangeNotification(savedTimetable, "UPDATED");
                    }

                    return savedTimetable;
                })
                .orElseThrow(() -> new RuntimeException("Timetable not found with id: " + timetableId));
    }

    public void deleteTimetable(String timetableId) {
        timetableRepository.deleteById(timetableId);
    }

    public void deactivateTimetable(String timetableId) {
        timetableRepository.findById(timetableId)
                .map(timetable -> {
                    timetable.setStatus("INACTIVE");
                    timetable.setUpdatedDate(LocalDate.now());
                    return timetableRepository.save(timetable);
                })
                .orElseThrow(() -> new RuntimeException("Timetable not found with id: " + timetableId));
    }

    // ===== Substitute Teacher Logic =====
    public List<Timetable> getSubstitutedTimetables() {
        return timetableRepository.findByIsSubstituted(true);
    }

    public List<Timetable> getTimetablesBySubstituteTeacher(String substituteTeacherId) {
        return timetableRepository.findBySubstituteTeacherId(substituteTeacherId);
    }

    public void removeSubstitute(String timetableId) {
        timetableRepository.findById(timetableId)
                .ifPresent(timetable -> {
                    timetable.setSubstituteTeacherId(null);
                    timetable.setIsSubstituted(false);
                    timetable.setSubstituteReason(null);
                    timetable.setSubstitutionDate(null);
                    timetable.setStatus("ACTIVE");
                    timetable.setUpdatedDate(LocalDate.now());

                    timetableRepository.save(timetable);
                    notificationService.createScheduleChangeNotification(timetable, "SUBSTITUTE_REMOVED");
                });
    }

    // ===== Timetable Queries =====
    public List<Timetable> getAllTimetables() {
        return timetableRepository.findAll();
    }

    public Optional<Timetable> getTimetableById(String timetableId) {
        return timetableRepository.findById(timetableId);
    }

    public List<Timetable> getTimetablesByDepartment(String departmentId) {
        return timetableRepository.findByDepartmentId(departmentId);
    }

    public List<Timetable> getTimetablesByTeacher(String teacherId) {
        return timetableRepository.findByTeacherId(teacherId);
    }

    public List<Timetable> getTimetablesBySemester(String semester) {
        return timetableRepository.findBySemester(semester);
    }

    public List<Timetable> getTimetablesByDepartmentAndSemester(String departmentId, String semester) {
        return timetableRepository.findByDepartmentIdAndSemester(departmentId, semester);
    }

    public List<Timetable> getTimetablesByDay(String dayOfWeek) {
        return timetableRepository.findByDayOfWeek(dayOfWeek);
    }

    public List<Timetable> getTimetablesByDepartmentAndDay(String departmentId, String dayOfWeek) {
        return timetableRepository.findByDepartmentIdAndDayOfWeek(departmentId, dayOfWeek);
    }

    public List<Timetable> getTimetablesByTeacherAndDay(String teacherId, String dayOfWeek) {
        return timetableRepository.findByTeacherIdAndDayOfWeek(teacherId, dayOfWeek);
    }

    public List<Timetable> getTimetablesByAcademicYear(String academicYear) {
        return timetableRepository.findByAcademicYear(academicYear);
    }

    public List<Timetable> getTimetablesByStatus(String status) {
        return timetableRepository.findByStatus(status);
    }

    public List<Timetable> getTimetablesByDepartmentSemesterAndSection(String departmentId, String semester, String section) {
        return timetableRepository.findByDepartmentIdAndSemesterAndSection(departmentId, semester, section);
    }

    // ===== Conflict Checks =====
    private boolean hasTeacherConflict(Timetable timetable) {
        return hasTeacherConflict(timetable, null);
    }

    private boolean hasTeacherConflict(Timetable timetable, String excludeId) {
        List<Timetable> conflicts = timetableRepository.findConflictingSchedules(
                timetable.getTeacherId(),
                timetable.getDayOfWeek(),
                timetable.getStartTime(),
                timetable.getEndTime()
        );

        if (excludeId != null) {
            conflicts.removeIf(t -> t.getTimetableId().equals(excludeId));
        }

        return !conflicts.isEmpty();
    }

    private boolean hasRoomConflict(Timetable timetable) {
        return hasRoomConflict(timetable, null);
    }

    private boolean hasRoomConflict(Timetable timetable, String excludeId) {
        List<Timetable> conflicts = timetableRepository.findRoomConflicts(
                timetable.getRoomNumber(),
                timetable.getDayOfWeek(),
                timetable.getStartTime(),
                timetable.getEndTime()
        );

        if (excludeId != null) {
            conflicts.removeIf(t -> t.getTimetableId().equals(excludeId));
        }

        return !conflicts.isEmpty();
    }
}

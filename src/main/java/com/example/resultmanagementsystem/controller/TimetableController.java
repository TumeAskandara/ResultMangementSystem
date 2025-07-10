package com.example.resultmanagementsystem.controller;
import com.example.resultmanagementsystem.model.Timetable;

import com.example.resultmanagementsystem.services.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TimetableController {

    private final TimetableService timetableService;

    @GetMapping("/substituted")
    public ResponseEntity<List<Timetable>> getSubstitutedTimetables() {
        List<Timetable> timetables = timetableService.getSubstitutedTimetables();
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/substitute-teacher/{substituteTeacherId}")
    public ResponseEntity<List<Timetable>> getTimetablesBySubstituteTeacher(@PathVariable String substituteTeacherId) {
        List<Timetable> timetables = timetableService.getTimetablesBySubstituteTeacher(substituteTeacherId);
        return ResponseEntity.ok(timetables);
    }

    @PatchMapping("/{id}/remove-substitute")
    public ResponseEntity<Void> removeSubstitute(@PathVariable String id) {
        try {
            timetableService.removeSubstitute(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Timetable>> getAllTimetables() {
        List<Timetable> timetables = timetableService.getAllTimetables();
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Timetable> getTimetableById(@PathVariable String id) {
        Optional<Timetable> timetable = timetableService.getTimetableById(id);
        return timetable.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Timetable>> getTimetablesByDepartment(@PathVariable String departmentId) {
        List<Timetable> timetables = timetableService.getTimetablesByDepartment(departmentId);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Timetable>> getTimetablesByTeacher(@PathVariable String teacherId) {
        List<Timetable> timetables = timetableService.getTimetablesByTeacher(teacherId);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<Timetable>> getTimetablesBySemester(@PathVariable String semester) {
        List<Timetable> timetables = timetableService.getTimetablesBySemester(semester);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/department/{departmentId}/semester/{semester}")
    public ResponseEntity<List<Timetable>> getTimetablesByDepartmentAndSemester(
            @PathVariable String departmentId,
            @PathVariable String semester) {
        List<Timetable> timetables = timetableService.getTimetablesByDepartmentAndSemester(departmentId, semester);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/day/{dayOfWeek}")
    public ResponseEntity<List<Timetable>> getTimetablesByDay(@PathVariable String dayOfWeek) {
        List<Timetable> timetables = timetableService.getTimetablesByDay(dayOfWeek);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/department/{departmentId}/day/{dayOfWeek}")
    public ResponseEntity<List<Timetable>> getTimetablesByDepartmentAndDay(
            @PathVariable String departmentId,
            @PathVariable String dayOfWeek) {
        List<Timetable> timetables = timetableService.getTimetablesByDepartmentAndDay(departmentId, dayOfWeek);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/teacher/{teacherId}/day/{dayOfWeek}")
    public ResponseEntity<List<Timetable>> getTimetablesByTeacherAndDay(
            @PathVariable String teacherId,
            @PathVariable String dayOfWeek) {
        List<Timetable> timetables = timetableService.getTimetablesByTeacherAndDay(teacherId, dayOfWeek);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/academic-year/{academicYear}")
    public ResponseEntity<List<Timetable>> getTimetablesByAcademicYear(@PathVariable String academicYear) {
        List<Timetable> timetables = timetableService.getTimetablesByAcademicYear(academicYear);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Timetable>> getTimetablesByStatus(@PathVariable String status) {
        List<Timetable> timetables = timetableService.getTimetablesByStatus(status);
        return ResponseEntity.ok(timetables);
    }

    @GetMapping("/department/{departmentId}/semester/{semester}/section/{section}")
    public ResponseEntity<List<Timetable>> getTimetablesByDepartmentSemesterAndSection(
            @PathVariable String departmentId,
            @PathVariable String semester,
            @PathVariable String section) {
        List<Timetable> timetables = timetableService.getTimetablesByDepartmentSemesterAndSection(departmentId, semester, section);
        return ResponseEntity.ok(timetables);
    }

    @PostMapping
    public ResponseEntity<Timetable> createTimetable(@RequestBody Timetable timetable) {
        try {
            Timetable createdTimetable = timetableService.createTimetable(timetable);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTimetable);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Timetable> updateTimetable(@PathVariable String id, @RequestBody Timetable timetable) {
        try {
            Timetable updatedTimetable = timetableService.updateTimetable(id, timetable);
            return ResponseEntity.ok(updatedTimetable);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable String id) {
        try {
            timetableService.deleteTimetable(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateTimetable(@PathVariable String id) {
        try {
            timetableService.deactivateTimetable(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


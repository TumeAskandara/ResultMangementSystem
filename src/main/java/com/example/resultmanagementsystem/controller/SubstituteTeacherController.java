package com.example.resultmanagementsystem.controller;


import com.example.resultmanagementsystem.model.SubstituteRequest;

import com.example.resultmanagementsystem.services.SubstituteTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/substitute-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubstituteTeacherController {

    private final SubstituteTeacherService substituteTeacherService;

    @GetMapping
    public ResponseEntity<List<SubstituteRequest>> getAllSubstituteRequests() {
        List<SubstituteRequest> requests = substituteTeacherService.getAllSubstituteRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubstituteRequest> getSubstituteRequestById(@PathVariable String id) {
        Optional<SubstituteRequest> request = substituteTeacherService.getSubstituteRequestById(id);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<SubstituteRequest>> getSubstituteRequestsByTeacher(@PathVariable String teacherId) {
        List<SubstituteRequest> requests = substituteTeacherService.getSubstituteRequestsByTeacher(teacherId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SubstituteRequest>> getSubstituteRequestsByStatus(@PathVariable String status) {
        List<SubstituteRequest> requests = substituteTeacherService.getSubstituteRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    @PostMapping
    public ResponseEntity<SubstituteRequest> createSubstituteRequest(@RequestBody SubstituteRequest request) {
        try {
            SubstituteRequest createdRequest = substituteTeacherService.createSubstituteRequest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<SubstituteRequest> approveSubstituteRequest(
            @PathVariable String id,
            @RequestParam String approvedBy) {
        try {
            SubstituteRequest approvedRequest = substituteTeacherService.approveSubstituteRequest(id, approvedBy);
            return ResponseEntity.ok(approvedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<SubstituteRequest> rejectSubstituteRequest(
            @PathVariable String id,
            @RequestParam String rejectedBy,
            @RequestParam String comments) {
        try {
            SubstituteRequest rejectedRequest = substituteTeacherService.rejectSubstituteRequest(id, rejectedBy, comments);
            return ResponseEntity.ok(rejectedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeSubstitution(@PathVariable String id) {
        try {
            substituteTeacherService.completeSubstitution(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

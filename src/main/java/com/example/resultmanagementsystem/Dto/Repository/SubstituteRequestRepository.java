package com.example.resultmanagementsystem.Dto.Repository;



import com.example.resultmanagementsystem.model.SubstituteRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubstituteRequestRepository extends MongoRepository<SubstituteRequest, String> {

    List<SubstituteRequest> findByOriginalTeacherId(String originalTeacherId);
    List<SubstituteRequest> findBySubstituteTeacherId(String substituteTeacherId);
    List<SubstituteRequest> findByTimetableId(String timetableId);
    List<SubstituteRequest> findByStatus(String status);
    List<SubstituteRequest> findBySubstituteDate(LocalDate substituteDate);
    List<SubstituteRequest> findByRequestDate(LocalDate requestDate);
}

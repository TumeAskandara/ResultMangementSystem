package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.AnalyticsReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsReportRepository extends MongoRepository<AnalyticsReport, String> {
    Page<AnalyticsReport> findByReportType(AnalyticsReport.ReportType reportType, Pageable pageable);
    List<AnalyticsReport> findByGeneratedBy(String generatedBy);
    List<AnalyticsReport> findByAcademicYear(String academicYear);
    List<AnalyticsReport> findByAcademicYearAndSemester(String academicYear, String semester);
}

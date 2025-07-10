package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.FeeCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeeCategoryRepository extends MongoRepository<FeeCategory, String> {

    List<FeeCategory> findByAcademicYear(String academicYear);

    List<FeeCategory> findByDepartmentId(String departmentId);

    List<FeeCategory> findByAcademicYearAndMandatory(String academicYear, boolean mandatory);

    @Query("{'academicYear': ?0, '$or': [{'departmentId': {'$in': ?1}}, {'departmentId': null}]}")
    List<FeeCategory> findByAcademicYearAndDepartmentIdInOrDepartmentIdIsNull(String academicYear, List<String> departmentIds);

    List<FeeCategory> findByAcademicYearAndDepartmentId(String academicYear, String departmentId);
}

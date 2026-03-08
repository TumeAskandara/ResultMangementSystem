package com.example.resultmanagementsystem.Dto.Repository;

import com.example.resultmanagementsystem.model.ClassPromotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassPromotionRepository extends MongoRepository<ClassPromotion, String> {
    List<ClassPromotion> findByStudentId(String studentId);
    List<ClassPromotion> findByFromClassId(String fromClassId);
    List<ClassPromotion> findByToClassId(String toClassId);
    List<ClassPromotion> findByFromAcademicYear(String fromAcademicYear);
    List<ClassPromotion> findByPromotionType(ClassPromotion.PromotionType promotionType);
}
